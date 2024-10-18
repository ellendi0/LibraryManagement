package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.data.ReservationDataFactory
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.domain.ReservationOutcome
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.LibraryService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ReservationServiceImplTest {
    private val reservationRepository: ReservationRepository = mockk()
    private val bookPresenceService: BookPresenceService = mockk()
    private val libraryService: LibraryService = mockk()

    private val reservationService = ReservationServiceImpl(
        reservationRepository,
        bookPresenceService,
        libraryService,
    )
    private val id = ReservationDataFactory.ID

    private val reservation = ReservationDataFactory.createReservation(id)
    private val journal = JournalDataFactory.createJournal(id)
    private val bookPresence = BookPresenceDataFactory.createBookPresence(id)

    @Test
    fun `should get reservation by user id`() {
        // GIVEN
        val expected = reservation

        every { reservationRepository.findAllByUserId(id) } returns Flux.just(reservation)

        // WHEN
        val result = StepVerifier.create(reservationService.getAllReservationsByUserId(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should borrow instead of reserving if book is available`() {
        // GIVEN
        val bookPresence = BookPresenceDataFactory.createBookPresence(id).copy(availability = Availability.AVAILABLE)

        val expected = ReservationOutcome.Journals(listOf(journal))

        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns Mono.just(false)
        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookPresenceService.existsBookPresenceByBookIdAndLibraryId(id, id) } returns Mono.just(true)
        every { bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(id, id) }
            .returns(Flux.just(bookPresence))
        every { bookPresenceService.borrowBookFromLibrary(id, id, id) } returns Flux.just(journal)

        // WHEN
        val result = StepVerifier.create(reservationService.reserveBook(id, id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { bookPresenceService.borrowBookFromLibrary(id, id, id) }
        }.verifyComplete()
    }

    @Test
    fun `should reserve if book is unavailable`() {
        // GIVEN
        val expected = ReservationOutcome.Reservations(listOf(reservation))

        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns Mono.just(false)
        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookPresenceService.existsBookPresenceByBookIdAndLibraryId(id, id) } returns Mono.just(true)
        every { bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(id, id) } returns Flux.just(bookPresence)
        every { reservationRepository.save(any()) } returns Mono.just(reservation)
        every { reservationService.getAllReservationsByUserId(id) } returns Flux.just(reservation)

        // WHEN
        val result = StepVerifier.create(reservationService.reserveBook(id, id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { reservationRepository.save(any()) }
        }.verifyComplete()
    }

    @Test
    fun `should not reserve book`() {
        // GIVEN

        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns Mono.just(false)
        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookPresenceService.existsBookPresenceByBookIdAndLibraryId(id, id) } returns Mono.just(true)
        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns Mono.just(true)

        // WHEN
        val result = StepVerifier.create(reservationService.reserveBook(id, id, id))

        // THEN
        result.expectErrorMatches { ex ->
            ex is ExistingReservationException
        }.verify()

        verify(exactly = 0) { reservationRepository.save(any()) }
    }


    @Test
    fun `should delete reservation by id`() {
        // GIVEN
        val expected = Unit

        every { reservationRepository.findById(id) } returns Mono.just(reservation)
        every { reservationRepository.deleteById(id) } returns Mono.just(Unit)

        // WHEN
        val result = StepVerifier.create(reservationService.deleteReservationById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { reservationRepository.deleteById(id) }
        }.verifyComplete()
    }

    @Test
    fun `should cancel reservation`() {
        // GIVEN
        val expected = Unit

        every { reservationRepository.findAllByBookIdAndUserId(id, id) } returns Flux.just(reservation)
        every { reservationRepository.deleteById(id) } returns Mono.just(Unit)

        // WHEN
        val result = StepVerifier.create(reservationService.cancelReservation(id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { reservationRepository.deleteById(id) }
        }.verifyComplete()
    }
}

