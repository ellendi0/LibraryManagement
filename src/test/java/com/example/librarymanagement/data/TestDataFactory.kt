package com.example.librarymanagement.data

import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBook
import com.example.librarymanagement.model.jpa.JpaBookPresence
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.model.jpa.JpaLibrary
import com.example.librarymanagement.model.jpa.JpaReservation
import com.example.librarymanagement.model.jpa.JpaUser

object TestDataFactory {

    fun createTestDataRelationsForJpaRepositories(): TestDataRel {
        val library = LibraryDataFactory.createJpaLibrary()
        val user = UserDataFactory.createJpaUser()
        val author = AuthorDataFactory.createJpaAuthor()
        val publisher = PublisherDataFactory.createJpaPublisher()
        val book = BookDataFactory.createJpaBook().copy(author = author, publisher = publisher)
        val bookPresence =
            BookPresenceDataFactory.createJpaBookPresence().copy(
                availability = Availability.UNAVAILABLE,
                book = book.copy(bookPresence = mutableListOf()),
                library = library.copy(bookPresence = mutableListOf()),
                user = user.copy(journals = mutableListOf(), reservations = mutableListOf())
            )
        val journal = JournalDataFactory.createJpaJournal().copy(user = user, bookPresence = bookPresence)
        val reservation =
            ReservationDataFactory.createJpaReservation().copy(user = user, book = book, library = library)
//
//        book.bookPresence.add(bookPresence)
//        book.reservations.add(reservation)
//        bookPresence.journals.add(journal)

        return TestDataRel(library, user, book, bookPresence, journal, reservation)
    }

//    fun createTestDataRelForController(): TestDataRel {
//        val library = LibraryDataFactory.createLibrary()
//        val user = UserDataFactory.createUser()
//        val book = BookDataFactory.createBook()
//        val bookPresence = BookPresenceDataFactory.createBookPresence()
//        val journal = JournalDataFactory.createJournal()
//        val reservation = ReservationDataFactory.createReservation()
////
////        book.bookPresence.add(bookPresence)
////        book.reservations.add(reservation)
////        bookPresence.journals.add(journal)
////        bookPresence.user = user
//
//        return TestDataRel(library, user, book, bookPresence, journal, reservation)
//    }

    data class TestDataRel(
        val library: JpaLibrary,
        val user: JpaUser,
        val book: JpaBook,
        val bookPresence: JpaBookPresence,
        val journal: JpaJournal,
        val reservation: JpaReservation,
    )
}
