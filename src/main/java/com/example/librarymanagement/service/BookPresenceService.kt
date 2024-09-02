package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.enums.Availability

interface BookPresenceService {
    fun addUserToBook(userId: String, libraryId: String, bookId: String): List<Journal>
    fun addBookToLibrary(libraryId: String, bookId: String): BookPresence
    fun removeUserFromBook(userId: String, libraryId: String, bookId: String): List<Journal>
    fun getAllByBookId(bookId: String): List<BookPresence>
    fun getAllByLibraryId(libraryId: String): List<BookPresence>
    fun getAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence>
    fun getAllBookPresencesByLibraryIdAndAvailability(libraryId: String, availability: Availability): List<BookPresence>
    fun deleteBookPresenceById(id: String)
    fun existsBookPresenceByBookIdAndLibraryId(bookId: String, libraryId: String): Boolean
}
