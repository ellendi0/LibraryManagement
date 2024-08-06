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
        every { bookPresenceRepository.save(bookPresence) } returns bookPresence

        Assertions.assertEquals(bookPresence, bookPresenceService.createBookPresence(bookPresence))
        verify(exactly = 1) { bookPresenceRepository.save(bookPresence) }
    }

    @Test
    fun shouldUpdateBookPresence() {
        val updatedBookPresence = bookPresence.copy(availability = Availability.AVAILABLE)
        every { bookPresenceRepository.findById(1) } returns Optional.of(bookPresence)
        every { bookPresenceRepository.save(updatedBookPresence) } returns updatedBookPresence

        Assertions.assertEquals(updatedBookPresence, bookPresenceService.updateBookPresence(1, updatedBookPresence))
        verify(exactly = 1) { bookPresenceRepository.findById(1) }
        verify(exactly = 1) { bookPresenceRepository.save(updatedBookPresence) }
    }

    @Test
    fun shouldAddUserToBookWithReservation() {
        val reservationTest = reservation.copy(user = user)

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

        Assertions.assertEquals(bookPresence, bookPresenceService.addUserToBook(user, library.id!!, book.id!!))
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

        Assertions.assertEquals(bookPresence, bookPresenceService.addUserToBook(user, library.id!!, book.id!!))
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
        val bookWithout = book.copy(bookPresence = mutableListOf(), reservations = mutableListOf())
        val libraryTest = library.copy(bookPresence = mutableListOf())
        val bookPresenceTest = BookPresence(book = bookWithout, library = libraryTest)

        every { bookRepository.findByIdOrNull(bookWithout.id!!) } returns bookWithout
        every { libraryRepository.findByIdOrNull(libraryTest.id!!) } returns libraryTest
        every { bookPresenceRepository.save(bookPresenceTest) } returns bookPresenceTest

        Assertions
            .assertEquals(bookPresenceTest, bookPresenceService.addBookToLibrary(libraryTest.id!!, bookWithout.id!!))
        verify(exactly = 1) { bookRepository.findByIdOrNull(bookWithout.id!!) }
        verify(exactly = 1) { libraryRepository.findByIdOrNull(library.id!!) }
    }

    @Test
    fun shouldRemoveUserFromBookWithReservation(){
        val reservationTest = reservation.copy(user = user)
        val journalTest = journal.copy(bookPresence = bookPresence)

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

        Assertions.assertEquals(bookPresence, bookPresenceService.removeUserFromBook(user, library.id!!, book.id!!))
    }

    @Test
    fun shouldRemoveUserFromBookWithoutReservation() {
        every { bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(library.id!!, book.id!!, Availability.UNAVAILABLE) }
            .returns(listOf(bookPresence))
        every { journalService.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.id!!, user.id!!) }
            .returns(journal)
        every { journalService.updateJournal(journal.id!!, journal) } returns journal
        every { bookPresenceRepository.save(bookPresence) } returns bookPresence
        every { bookPresenceRepository.findById(bookPresence.id!!) } returns Optional.of(bookPresence)

        Assertions.assertEquals(bookPresence, bookPresenceService.removeUserFromBook(user, library.id!!, book.id!!))

        verify(exactly = 1) { bookPresenceRepository
            .findAllByLibraryIdAndBookIdAndAvailability(library.id!!, book.id!!, Availability.UNAVAILABLE) }
        verify(exactly = 1) { journalService
            .findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresence.id!!, user.id!!) }
        verify(exactly = 2) { bookPresenceRepository.save(bookPresence) }
        verify(exactly = 1) { bookPresenceRepository.findById(bookPresence.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryId(){
        every { bookPresenceRepository.findAllByLibraryId(library.id!!) } returns listOf(bookPresence)

        Assertions.assertEquals(listOf(bookPresence), bookPresenceService.getByLibraryId(library.id!!))
        verify(exactly = 1) { bookPresenceRepository.findAllByLibraryId(library.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByBookId(){
        every { bookPresenceRepository.findAllByBookId(book.id!!) } returns listOf(bookPresence)

        Assertions.assertEquals(listOf(bookPresence), bookPresenceService.getByBookId(book.id!!))
        verify(exactly = 1) { bookPresenceRepository.findAllByBookId(book.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryIdAndBookId() {
        every { bookPresenceRepository.findAllByLibraryIdAndBookId(library.id!!, book.id!!) } returns listOf(
            bookPresence
        )

        Assertions.assertEquals(listOf(bookPresence),
            bookPresenceService.getAllBookByLibraryIdAndBookId(library.id!!, book.id!!))
        verify(exactly = 1) { bookPresenceRepository.findAllByLibraryIdAndBookId(library.id!!, book.id!!) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryIdAndAvailability() {
        every { bookPresenceRepository.findAllByLibraryIdAndAvailability(library.id!!, Availability.AVAILABLE) }
            .returns( listOf(bookPresence))

        Assertions.assertEquals(listOf(bookPresence),
            bookPresenceService.getAllBookByLibraryIdAndAvailability(library.id!!, Availability.AVAILABLE))
        verify(exactly = 1) {
            bookPresenceRepository.findAllByLibraryIdAndAvailability(library.id!!, Availability.AVAILABLE)
        }
    }

    @Test
    fun shouldDeleteBookPresenceByIdAndLibraryId() {
        every { bookPresenceRepository.deleteBookPresenceByIdAndLibraryId(book.id!!, library.id!!) } returns Unit

        bookPresenceService.deleteBookPresenceByIdAndLibraryId(library.id!!, book.id!!)
        verify(exactly = 1) { bookPresenceRepository.deleteBookPresenceByIdAndLibraryId(book.id!!, library.id!!) }
    }

    @Test
    fun shouldDeleteBookPresenceById() {
        every { bookPresenceRepository.deleteById(bookPresence.id!!) } returns Unit

        bookPresenceService.deleteBookPresenceById(bookPresence.id!!)
        verify(exactly = 1) { bookPresenceRepository.deleteById(bookPresence.id!!) }
    }
}
