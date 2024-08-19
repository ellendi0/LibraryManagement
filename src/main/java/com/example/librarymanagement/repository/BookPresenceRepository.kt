package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability

interface BookPresenceRepository {
    fun save(bookPresence: BookPresence): BookPresence
    fun deleteById(bookPresenceId: String)
    fun addUserToBook(user: User, libraryId: String, bookId: String): BookPresence
    fun addBookToLibrary(libraryId: String, bookId: String): BookPresence
    fun removeUserFromBook(user: User, libraryId: String, bookId: String): BookPresence
    fun findAllByBookId(bookId: String): List<BookPresence>
    fun findAllByLibraryId(libraryId: String): List<BookPresence>
    fun findAllByUserId(userId: String): List<BookPresence>
    fun findAllByLibraryIdAndBookId(libraryId: String, bookId: String): List<BookPresence>
    fun findAllByLibraryIdAndBookIdAndAvailability(
            libraryId: String,
            bookId: String,
            availability: Availability): List<BookPresence>
    fun findAllByLibraryIdAndAvailability(libraryId: String, availability: Availability): List<BookPresence>
}
