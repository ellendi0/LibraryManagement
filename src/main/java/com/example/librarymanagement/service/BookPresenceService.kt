package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability

interface BookPresenceService {
    fun addUserToBook(user: User, libraryId: String, bookId: String): BookPresence
    fun addBookToLibrary(libraryId: String, bookId: String): BookPresence
    fun removeUserFromBook(user: User, libraryId: String, bookId: String): BookPresence
    fun getByBookId(bookId: String): List<BookPresence>
    fun getByLibraryId(libraryId: String): List<BookPresence>
    fun getAllBookByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence>
    fun getAllBookByLibraryIdAndAvailability(libraryId: String, availability: Availability): List<BookPresence>
    fun deleteBookPresenceById(id: String)
}

