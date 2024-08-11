package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.JournalService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class BookPresenceImplTest {
    private val bookPresenceRepository: BookPresenceRepository = mockk()
    private val journalService: JournalService = mockk()
    private val libraryRepository: LibraryRepository = mockk()
    private val reservationRepository: ReservationRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val bookPresenceService = BookPresenceServiceImpl(
        bookPresenceRepository,
        journalService,
        reservationRepository,
        bookRepository,
        libraryRepository
    )

    private lateinit var bookPresence: BookPresence
    private lateinit var book: Book
    private lateinit var user: User
    private lateinit var journal: Journal
    private lateinit var library: Library
    private lateinit var reservation: Reservation

    @BeforeEach
    fun setUp() {
        val test = TestDataFactory.createTestDataRelForServices()
        book = test.book
        user = test.user
        library = test.library
        bookPresence = test.bookPresence
        journal = test.journal
        reservation = test.reservation
    }

    @Test
    fun shouldCreateBookPresence() {
        //GIVEN
        val expected = bookPresence

        every { bookPresenceRepository.save(bookPresence) } returns bookPresence

        //WHEN
        val actual = bookPresenceService.createBookPresence(bookPresence)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookPresenceRepository.save(bookPresence) }
    }

    @Test
    fun shouldUpdateBookPresence() {
        //GIVEN
        val expected = bookPresence.copy(availability = Availability.AVAILABLE)

        every { bookPresenceRepository.findById(1) } returns Optional.of(bookPresence)
        every { bookPresenceRepository.save(expected) } returns expected

        //WHEN
        val actual = bookPresenceService.updateBookPresence(1, expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookPresenceRepository.findById(1) }
        verify(exactly = 1) { bookPresenceRepository.save(expected) }
    }

    @Test
    fun shouldAddUserToBookWithReservation() {
        //GIVEN
        val reservationTest = reservation.copy(user = user)
        val expected = bookPresence

        every {
            bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                library.id!!,
                book.id!!,
                Availability.AVAILABLE
            )
        }
            .returns(listOf(bookPresence))

        every { reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(book.id!!, library.id)}
            .returns (reservationTest)
        every { reservationRepository.delete(reservationTest) } returns Unit
        every { bookPresenceRepository.save(any()) } returns bookPresence
        every { journalService.createJournal(any()) } returns journal

        //WHEN
        val actual = bookPresenceService.addUserToBook(user, library.id!!, book.id!!)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                library.id!!,
                book.id!!,
                Availability.AVAILABLE
            )
        }
        verify(exactly = 1) { bookPresenceRepository.save(any()) }
        verify(exactly = 1) { journalService.createJournal(any()) }
    }

    @Test
    fun shouldAddUserToBookWithoutReservation() {
        //GIVEN
        val expected = bookPresence

        every {
            bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                library.id!!,
                book.id!!,
                Availability.AVAILABLE
            )
        }
            .returns(listOf(bookPresence))

        every { reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(book.id!!, library.id)}
            .returns (null)
        every { bookPresenceRepository.save(any()) } returns bookPresence
        every { journalService.createJournal(any()) } returns journal

        //WHEN
        val actual = bookPresenceService.addUserToBook(user, library.id!!, book.id!!)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            bookPresenceRepository.findAllByLibraryIdAndBookIdAndAvailability(
                library.id!!,
                book.id!!,
                Availability.AVAILABLE
            )
        }
        verify(exactly = 1) { bookPresenceRepository.save(any()) }
        verify(exactly = 1) { journalService.createJournal(any()) }
    }

    @Test
    fun shouldAddBookToLibrary(){
        //GIVEN
        val bookWithout = book.copy(bookPresence = mutableListOf(), reservations = mutableListOf())
        val libraryTest = library.copy(bookPresence = mutableListOf())
        val expected = BookPresence(book = bookWithout, library = libraryTest)

        every { bookRepository.findByIdOrNull(bookWithout.id!!) } returns bookWithout
        every { libraryRepository.findByIdOrNull(libraryTest.id!!) } returns libraryTest
        every { bookPresenceRepository.save(expected) } returns expected

        //WHEN
        val actual = bookPresenceService.addBookToLibrary(libraryTest.id!!, bookWithout.id!!)

        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookRepository.findByIdOrNull(bookWithout.id!!) }
        verify(exactly = 1) { libraryRepository.findByIdOrNull(library.id!!) }
    }

    @Test
    fun shouldRemoveUserFromBookWithReservation(){
        //GIVEN
        val reservationTest = reservation.copy(user = user)
        val journalTest = journal.copy(bookPresence = bookPresence)
        val expected = bookPresence

        every { bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(library.id!!, book.id!!, Availability.UNAVAILABLE) }
            .returns(listOf(bookPresence))
        every { journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.id!!, user.id!!) }
            .returns(journalTest)
        every { journalService.updateJournal(journal.id!!, journalTest) } returns journalTest
        every { bookPresenceRepository.save(bookPresence) } returns bookPresence
        every { reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(book.id!!, library.id!!) }
            .returns(reservationTest)
        every { bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(library.id!!, book.id!!, Availability.AVAILABLE) }
            .returns(listOf(bookPresence))
        every { journalService.createJournal(any()) } returns journal
        every { reservationRepository.delete(reservationTest) } returns Unit
        every { bookPresenceRepository.findById(bookPresence.id!!) } returns Optional.of(bookPresence)

        //WHEN
        val actual = bookPresenceService.removeUserFromBook(user, library.id!!, book.id!!)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldRemoveUserFromBookWithoutReservation() {
        //GIVEN
        val expected = bookPresence

        every { bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(library.id!!, book.id!!, Availability.UNAVAILABLE) }
            .returns(listOf(bookPresence))
        every { journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.id!!, user.id!!) }
            .returns(journal)
        every { journalService.updateJournal(journal.id!!, journal) } returns journal
        every { bookPresenceRepository.save(bookPresence) } returns bookPresence
        every { bookPresenceRepository.findById(bookPresence.id!!) } returns Optional.of(bookPresence)

        //WHEN
        val actual = bookPresenceService.removeUserFromBook(user, library.id!!, book.id!!)

        //THEN
        Assertions.assertEquals(expected, actual)

        verify(exactly = 1) { bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(library.id!!, book.id!!, Availability.UNAVAILABLE) }
        verify(exactly = 1) { journalService
            .findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.id!!, user.id!!) }
        verify(exactly = 2) { bookPresenceRepository.save(bookPresence) }
        verify(exactly = 1) { bookPresenceRepository.findById(bookPresence.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryId(){
        //GIVEN
        val excepted = listOf(bookPresence)

        every { bookPresenceRepository.findAllByLibraryId(library.id!!) } returns listOf(bookPresence)

        //WHEN
        val actual = bookPresenceService.getByLibraryId(library.id!!)

        //THEN
        Assertions.assertEquals(excepted, actual)
        verify(exactly = 1) { bookPresenceRepository.findAllByLibraryId(library.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByBookId(){
        //GIVEN
        val excepted = listOf(bookPresence)

        every { bookPresenceRepository.findAllByBookId(book.id!!) } returns listOf(bookPresence)

        //WHEN
        val actual = bookPresenceService.getByBookId(book.id!!)

        Assertions.assertEquals(excepted, actual)
        verify(exactly = 1) { bookPresenceRepository.findAllByBookId(book.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryIdAndBookId() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { bookPresenceRepository.findAllByLibraryIdAndBookId(library.id!!, book.id!!) } returns listOf(
            bookPresence
        )

        //WHEN
        val actual = bookPresenceService.getAllBookByLibraryIdAndBookId(library.id!!, book.id!!)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookPresenceRepository.findAllByLibraryIdAndBookId(library.id!!, book.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryIdAndAvailability() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { bookPresenceRepository.findAllByLibraryIdAndAvailability(library.id!!, Availability.AVAILABLE) }
            .returns( listOf(bookPresence))

        //WHEN
        val actual = bookPresenceService.getAllBookByLibraryIdAndAvailability(library.id!!, Availability.AVAILABLE)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            bookPresenceRepository.findAllByLibraryIdAndAvailability(library.id!!, Availability.AVAILABLE)
        }
    }

    @Test
    fun shouldDeleteBookPresenceByIdAndLibraryId() {
        //GIVEN
        every { bookPresenceRepository.deleteBookPresenceByIdAndLibraryId(book.id!!, library.id!!) } returns Unit

        //WHEN
        bookPresenceService.deleteBookPresenceByIdAndLibraryId(library.id!!, book.id!!)

        //THEN
        verify(exactly = 1) { bookPresenceRepository.deleteBookPresenceByIdAndLibraryId(book.id!!, library.id!!) }
    }

    @Test
    fun shouldDeleteBookPresenceById() {
        //GIVEN
        every { bookPresenceRepository.deleteById(bookPresence.id!!) } returns Unit

        //WHEN
        bookPresenceService.deleteBookPresenceById(bookPresence.id!!)

        //THEN
        verify(exactly = 1) { bookPresenceRepository.deleteById(bookPresence.id!!) }
    }
}
