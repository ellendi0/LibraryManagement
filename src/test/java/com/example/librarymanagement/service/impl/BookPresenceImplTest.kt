package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.BookDataFactory
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
    private val id = "1"

    private val bookPresence = BookPresenceDataFactory.createBookPresence(id)
    private val book = BookDataFactory.createBook(id)
    private val user = UserDataFactory.createUser(id)
    private val journal = JournalDataFactory.createJournal(id)
    private val library = LibraryDataFactory.createLibrary(id)
    private val reservation = ReservationDataFactory.createReservation(id)

    @Test
    fun shouldAddUserToBookWithReservation() {
        //GIVEN
        val reservationTest = reservation.copy(userId = id)
        val expected = listOf(journal)

        every { userService.getUserById(id) } returns user
        every { libraryService.getLibraryById(id) } returns library
        every { reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(id, id) }
            .returns(reservationTest)
        every { reservationRepository.deleteById(id) } returns Unit
        every { bookPresenceRepository.addBookToUser(user, id, id) } returns bookPresence
        every { journalService.getJournalByUserId(id) } returns listOf(journal)

        //WHEN
        val actual = bookPresenceService.addUserToBook(id, id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { reservationRepository.deleteById(id) }
        verify(exactly = 1) { bookPresenceRepository.addBookToUser(user, id, id) }
        verify(exactly = 1) { journalService.getJournalByUserId(id) }
    }

    @Test
    fun shouldAddUserToBookWithoutReservation() {
        //GIVEN
        val expected = listOf(journal)

        every { userService.getUserById(id) } returns user
        every { libraryService.getLibraryById(id) } returns library
        every { reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(id, id) }
            .returns(null)
        every { bookPresenceRepository.addBookToUser(user, id, id) } returns bookPresence
        every { journalService.getJournalByUserId(id) } returns listOf(journal)

        //WHEN
        val actual = bookPresenceService.addUserToBook(id, id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { userService.getUserById(id) }
        verify(exactly = 1) { libraryService.getLibraryById(id) }
        verify(exactly = 1) { reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(id, id) }
        verify(exactly = 1) { bookPresenceRepository.addBookToUser(user, id, id) }
        verify(exactly = 1) { journalService.getJournalByUserId(id) }
    }

    @Test
    fun shouldAddBookToLibrary() {
        //GIVEN
        val bookWithout = book
        val libraryTest = library
        val expected = BookPresence(bookId = id, libraryId = id)

        every { bookService.getBookById(id) } returns bookWithout
        every { libraryService.getLibraryById(id) } returns libraryTest
        every { bookPresenceRepository.save(expected) } returns expected

        //WHEN
        val actual = bookPresenceService.addBookToLibrary(id, id)

        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookService.getBookById(id) }
        verify(exactly = 1) { libraryService.getLibraryById(id) }
        verify(exactly = 1) { bookPresenceRepository.save(expected) }
    }

    @Test
    fun shouldRemoveUserFromBookWithReservation() {
        //GIVEN
        val expected = listOf(journal)

        every { userService.getUserById(id) } returns user
        every { libraryService.getLibraryById(id) } returns library
        every { bookPresenceRepository.removeBookFromUser(user, id, id) } returns bookPresence
        every { journalService.getJournalByUserId(id) } returns listOf(journal)

        //WHEN
        val actual = bookPresenceService.removeUserFromBook(id, id, id)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetBookPresenceByLibraryId() {
        //GIVEN
        val excepted = listOf(bookPresence)

        every { bookPresenceRepository.findAllByLibraryId(id) } returns listOf(bookPresence)

        //WHEN
        val actual = bookPresenceService.getAllByLibraryId(id)

        //THEN
        Assertions.assertEquals(excepted, actual)
        verify(exactly = 1) { bookPresenceRepository.findAllByLibraryId(id) }
    }

    @Test
    fun shouldGetBookPresenceByBookId() {
        //GIVEN
        val excepted = listOf(bookPresence)

        every { bookPresenceRepository.findAllByBookId(id) } returns listOf(bookPresence)

        //WHEN
        val actual = bookPresenceService.getAllByBookId(id)

        Assertions.assertEquals(excepted, actual)
        verify(exactly = 1) { bookPresenceRepository.findAllByBookId(id) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryIdAndBookId() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { bookPresenceRepository.findAllByLibraryIdAndBookId(id, id) } returns listOf(
            bookPresence
        )

        //WHEN
        val actual = bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(id, id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { bookPresenceRepository.findAllByLibraryIdAndBookId(id, id) }
    }

    @Test
    fun shouldGetBookPresenceByLibraryIdAndAvailability() {
        //GIVEN
        val expected = listOf(bookPresence)

        every { bookPresenceRepository.findAllByLibraryIdAndAvailability(id, Availability.AVAILABLE) }
            .returns(listOf(bookPresence))

        //WHEN
        val actual = bookPresenceService.getAllBookPresencesByLibraryIdAndAvailability(id, Availability.AVAILABLE)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            bookPresenceRepository.findAllByLibraryIdAndAvailability(id, Availability.AVAILABLE)
        }
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
