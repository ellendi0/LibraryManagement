package com.example.librarymanagement.data

import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.enums.Availability

object BookPresenceDataFactory {
    fun createBookPresence(): BookPresence {
        return BookPresence(
            id = 1L,
            availability = Availability.AVAILABLE,
            book = BookDataFactory.createBook(),
            library = LibraryDataFactory.createLibrary(),
            user = UserDataFactory.createUser()
        )
    }
}
