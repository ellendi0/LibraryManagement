package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.model.enums.Availability

interface BookPresenceService {
    fun createBookPresence(bookPresence: BookPresence): BookPresence
    fun updateBookPresence(id:Long, bookPresenceToUpdate: BookPresence): BookPresence
    fun addUserToBook(user: User, libraryId: Long, bookId: Long): BookPresence
    fun addBookToLibrary(libraryId: Long, bookId: Long): BookPresence
    fun removeUserFromBook(user: User, libraryId: Long, bookId: Long): BookPresence
    fun getByBookId(bookId: Long): List<BookPresence>
    fun getByLibraryId(libraryId: Long): List<BookPresence>
    fun getAllBookByLibraryIdAndBookId(libraryId: Long, bookId: Long): List<BookPresence>
    fun getAllBookByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<BookPresence>
    fun findAllByLibraryIdAndBookIdAndAvailability(libraryId: Long,
                                                   bookId: Long,
                                                   availability: Availability): List<BookPresence>
    fun deleteBookPresenceByIdAndLibraryId(libraryId: Long, bookId: Long)
    fun deleteBookPresenceById(id: Long)
}
