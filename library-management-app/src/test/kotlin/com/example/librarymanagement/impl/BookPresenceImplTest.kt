package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.data.ReservationDataFactory
import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookService
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.LibraryService
import com.example.librarymanagement.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class BookPresenceImplTest {
    private val bookPresenceRepository: BookPresenceRepository = mockk()
    private val journalService: JournalService = mockk()
    private val libraryService: LibraryService = mockk()
    private val reservationRepository: ReservationRepository = mockk()
    private val bookService: BookService = mockk()
    private val userService: UserService = mockk()
    private val bookPresenceService = BookPresenceServiceImpl(
        bookPresenceRepository,
        journalService,
        reservationRepository,
        bookService,
        libraryService,
        userService
    )
    private val id = BookPresenceDataFactory.ID

    private val bookPresence = BookPresenceDataFactory.createBookPresence(id)
    private val user = UserDataFactory.createUser(id)
    private val journal = JournalDataFactory.createJournal(id)
    private val library = LibraryDataFactory.createLibrary(id)
    private val reservation = ReservationDataFactory.createReservation(id)

    @Test
    fun `should add user to book with reservation`() {
        // GIVEN
        val reservationTest = reservation.copy(userId = id)
        val expected = journal

        every { userService.existsUserById(id) } returns Mono.just(true)
        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookService.existsBookById(id) } returns Mono.just(true)
        every { userService.getUserById(id) } returns Mono.just(user)
        every { libraryService.getLibraryById(id) } returns Mono.just(library)
        every { reservationRepository.findFirstByBookIdAndLibraryId(id, id) }
            .returns(Mono.just(reservationTest))
        every { reservationRepository.deleteById(id) } returns Mono.just(Unit)
        every { bookPresenceRepository.addBookToUser(id, id, id) } returns Mono.just(bookPresence)
        every { journalService.getJournalByUserId(id) } returns Flux.just(journal)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.borrowBookFromLibrary(id, id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { reservationRepository.deleteById(id) }
            verify(exactly = 1) { bookPresenceRepository.addBookToUser(id, id, id) }
            verify(exactly = 1) { journalService.getJournalByUserId(id) }
        }.verifyComplete()
    }

    @Test
    fun `should add user to book without reservation`() {
        // GIVEN
        val expected = journal

        every { userService.existsUserById(id) } returns Mono.just(true)
        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookService.existsBookById(id) } returns Mono.just(true)
        every { userService.getUserById(id) } returns Mono.just(user)
        every { libraryService.getLibraryById(id) } returns Mono.just(library)
        every { reservationRepository.findFirstByBookIdAndLibraryId(id, id) }
            .returns(Mono.empty())
        every { bookPresenceRepository.addBookToUser(id, id, id) } returns Mono.just(bookPresence)
        every { journalService.getJournalByUserId(id) } returns Flux.just(journal)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.borrowBookFromLibrary(id, id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { reservationRepository.findFirstByBookIdAndLibraryId(id, id) }
            verify(exactly = 1) { bookPresenceRepository.addBookToUser(id, id, id) }
            verify(exactly = 1) { journalService.getJournalByUserId(id) }
        }.verifyComplete()
    }

    @Test
    fun `should add book to library`() {
        // GIVEN
        val expected = BookPresence(bookId = id, libraryId = id)

        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookService.existsBookById(id) } returns Mono.just(true)
        every { bookPresenceRepository.saveOrUpdate(expected) } returns Mono.just(expected)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.addBookToLibrary(id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { bookPresenceRepository.saveOrUpdate(expected) }
        }.verifyComplete()
    }

    @Test
    fun `should remove user from book with reservation`() {
        // GIVEN
        val reservationTest = reservation.copy(userId = id)
        val expected = journal

        every { userService.existsUserById(id) } returns Mono.just(true)
        every { libraryService.existsLibraryById(id) } returns Mono.just(true)
        every { bookService.existsBookById(id) } returns Mono.just(true)
        every { bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(id, id, Availability.UNAVAILABLE) }
            .returns(Flux.just(bookPresence))
        every { journalService.getByBookPresenceIdAndUserIdAndDateOfReturningIsNull(id, id) }
            .returns (Mono.just(journal))
        every { journalService.save(journal) } returns Mono.just(journal)
        every { bookPresenceRepository.saveOrUpdate(bookPresence) } returns Mono.just(bookPresence)
        every { journalService.getJournalByUserId(id) } returns Flux.just(journal)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.returnBookToLibrary(id, id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { journalService.save(journal) }
            verify(exactly = 1) { bookPresenceRepository.saveOrUpdate(bookPresence) }
        }.verifyComplete()
    }

    @Test
    fun `should get book presence by library id`() {
        // GIVEN
        val expected = bookPresence

        every { bookPresenceRepository.findAllByLibraryId(id) } returns Flux.just(bookPresence)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.getAllByLibraryId(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should get book presence by book id`() {
        // GIVEN
        val expected = bookPresence

        every { bookPresenceRepository.findAllByBookId(id) } returns Flux.just(bookPresence)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.getAllByBookId(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }


    @Test
    fun `should get book presence by library id and book id`() {
        // GIVEN
        val expected = bookPresence

        every { bookPresenceRepository.findAllByLibraryIdAndBookId(id, id) } returns Flux.just(bookPresence)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should delete book presence by id`() {
        // GIVEN
        val expected = Unit
        every { bookPresenceRepository.deleteById(bookPresence.id!!) } returns Mono.just(Unit)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.deleteBookPresenceById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should exists book presence by book id and library id`() {
        // GIVEN
        val expected = true

        every { bookPresenceRepository.existsByBookIdAndLibraryId(id, id) } returns Mono.just(true)

        // WHEN
        val result = StepVerifier.create(bookPresenceService.existsBookPresenceByBookIdAndLibraryId(id, id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }
}
