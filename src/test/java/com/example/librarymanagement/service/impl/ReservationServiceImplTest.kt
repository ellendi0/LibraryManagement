package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.BookDataFactory
import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.data.ReservationDataFactory
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.domain.ReservationOutcome
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.LibraryService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReservationServiceImplTest {
    private val reservationRepository: ReservationRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val bookPresenceService: BookPresenceService = mockk()
    private val journalService: JournalService = mockk()
    private val libraryService: LibraryService = mockk()

    private val reservationService = ReservationServiceImpl(
        reservationRepository,
        bookPresenceService,
        libraryService,
        journalService
    )
    private val id = "1"

    private val reservation = ReservationDataFactory.createReservation(id)
    private val journal = JournalDataFactory.createJournal(id)

    @Test
    fun shouldGetReservationByUserId() {
        //GIVEN
        val expected = listOf(reservation)
        every { reservationRepository.findAllByUserId(id) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.getReservationsByUserId(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { reservationRepository.findAllByUserId(id) }
    }

    @Test
    fun shouldBorrowInsteadOdReservingIfBookIsAvailable() {
        //GIVEN
        val library = LibraryDataFactory.createLibrary(id)
        val bookPresence = BookPresenceDataFactory.createBookPresence(id).copy(availability = Availability.AVAILABLE)
        val book = BookDataFactory.createBook(id)

        val expected = ReservationOutcome.Journals(listOf(journal))

        every { bookRepository.findById(id) } returns (book)
        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns false
        every { libraryService.getLibraryById(id) } returns library
        every { bookPresenceService.existsBookPresenceByBookIdAndLibraryId(id, id) } returns true
        every { bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(id, id) }
            .returns(listOf(bookPresence))
        every { bookPresenceService.addUserToBook(id, id, id) } returns listOf(journal)
        every { journalService.getJournalByUserId(id) } returns listOf(journal)

        //WHEN
        val actual = reservationService.reserveBook(id, id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookPresenceService.addUserToBook(id, id, id) }
        verify(exactly = 0) { reservationRepository.save(any()) }
    }

    @Test
    fun shouldReserveIfBookIsUnavailable() {
        //GIVEN
        val expected = ReservationOutcome.Reservations(listOf(reservation))

        val book = BookDataFactory.createBook(id)
        val library = LibraryDataFactory.createLibrary(id)
        val bookPresence = BookPresenceDataFactory.createBookPresence(id)

        every { bookRepository.findById(id) } returns (book)
        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns false
        every { libraryService.getLibraryById(id) } returns library
        every { bookPresenceService.existsBookPresenceByBookIdAndLibraryId(id, id) } returns true
        every { reservationRepository.findAllByBookIdAndLibraryId(id, id) } returns listOf(reservation)
        every { bookPresenceService.getAllByBookId(id) } returns listOf(bookPresence)
        every { bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(id, id) }
            .returns(emptyList())
        every { reservationRepository.save(any()) } returns reservation
        every { reservationService.getReservationsByUserId(id) } returns listOf(reservation)

        //WHEN
        val actual = reservationService.reserveBook(id, id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { reservationRepository.save(any()) }
        verify(exactly = 0) { bookPresenceService.addUserToBook(id, id, id) }
    }

    @Test
    fun shouldNotReserveBook() {
        //GIVEN
        val expected = ExistingReservationException::class.java
        val book = BookDataFactory.createBook(id)

        every { bookRepository.findById(id) } returns (book)
        every { reservationRepository.existsByBookIdAndUserId(id, id) } returns true

        //THEN
        Assertions.assertThrows(expected) {
            reservationService.reserveBook(id, id, id)
        }
    }

    @Test
    fun shouldDeleteReservationById() {
        //GIVEN
        every { reservationRepository.findById(id) } returns (reservation)
        every { reservationRepository.deleteById(id) } returns Unit

        //WHEN
        reservationService.deleteReservationById(id)

        //THEN
        verify(exactly = 1) { reservationRepository.deleteById(id) }
    }

    @Test
    fun shouldCancelReservation() {
        //GIVEN
        every { reservationRepository.findAllByBookIdAndUserId(id, id) } returns listOf(reservation)
        every { reservationRepository.deleteById(any()) } returns Unit

        //WHEN
        reservationService.cancelReservation(id, id)

        //THEN
        verify(exactly = 1) { reservationRepository.deleteById(id) }
    }
}
