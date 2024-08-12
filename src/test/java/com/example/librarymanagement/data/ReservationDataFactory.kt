package com.example.librarymanagement.data

import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User

object ReservationDataFactory {
    fun createReservation(): Reservation {
        return Reservation(
            id = 1L,
            book = BookDataFactory.createBook(),
            library = LibraryDataFactory.createLibrary()
        )
    }

    fun Reservation.withBookUserLibrary(book: Book = BookDataFactory.createBook(),
                             user: User = UserDataFactory.createUser(),
                             library: Library = LibraryDataFactory.createLibrary()): Reservation {

        return copy(book = book, user = user, library = library)
    }
}
