package com.example.librarymanagement.data

import com.example.librarymanagement.data.JournalDataFactory.withUserAndBookPresence
import com.example.librarymanagement.data.ReservationDataFactory.withBookUserLibrary
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User

object TestDataFactory {
    fun createTestDataRelForServices(): TestDataRel {
        val library = LibraryDataFactory.createLibrary()
        val user = UserDataFactory.createUser()
        val book = BookDataFactory.createBook()
        val bookPresence = BookPresenceDataFactory.createBookPresence()
        val journal = JournalDataFactory.createJournal()
        val reservation = ReservationDataFactory.createReservation()

        user.journals.add(journal)
        user.reservations.add(reservation)
        book.bookPresence.add(bookPresence)
        book.reservations.add(reservation)
        bookPresence.journals.add(journal)
        library.bookPresence.add(bookPresence)

        return TestDataRel(library, user, book, bookPresence, journal, reservation)
    }

    fun createTestDataRelForController(): TestDataRel {
        val library = LibraryDataFactory.createLibrary()
        val user = UserDataFactory.createUser()
        val book = BookDataFactory.createBook()
        val bookPresence = BookPresenceDataFactory.createBookPresence()
        val journal = JournalDataFactory.createJournal().withUserAndBookPresence(user, bookPresence)
        val reservation = ReservationDataFactory.createReservation().withBookUserLibrary(book, user, library)

        user.journals.add(journal)
        user.reservations.add(reservation)
        book.bookPresence.add(bookPresence)
        book.reservations.add(reservation)
        bookPresence.journals.add(journal)
        bookPresence.user = user
        library.bookPresence.add(bookPresence)

        return TestDataRel(library, user, book, bookPresence, journal, reservation)
    }

    data class TestDataRel(
        val library: Library,
        val user: User,
        val book: Book,
        val bookPresence: BookPresence,
        val journal: Journal,
        val reservation: Reservation
    )
}
