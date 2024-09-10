package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.enums.Availability

interface BookPresenceRepository {
    fun save(bookPresence: BookPresence): BookPresence
    fun addBookToUser(user: User, libraryId: String, bookId: String): BookPresence?
    fun removeBookFromUser(user: User, libraryId: String, bookId: String): BookPresence?
    fun findById(bookPresenceId: String): BookPresence?
    fun deleteById(bookPresenceId: String)
    fun findAllByBookId(bookId: String): List<BookPresence>
    fun findAllByLibraryId(libraryId: String): List<BookPresence>
    fun findAllByUserId(userId: String): List<BookPresence>
    fun findAllByLibraryIdAndBookId(
        libraryId: String,
        bookId: String
    ): List<BookPresence>

    fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): List<BookPresence>

    fun findAllByLibraryIdAndAvailability(libraryId: String, availability: Availability): List<BookPresence>
    fun existsByBookIdAndLibraryId(bookId: String, libraryId: String): Boolean

}
