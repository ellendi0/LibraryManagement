package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.model.enums.Availability
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookPresenceRepository : JpaRepository<BookPresence, Long> {
    fun findAllByBookId(bookId: Long): List<BookPresence>
    fun findAllByLibraryId(libraryId: Long): List<BookPresence>
    fun findAllByUserId(userId: Long): List<BookPresence>
    fun findAllByLibraryIdAndBookId(libraryId: Long, bookId: Long): List<BookPresence>
    fun findAllByLibraryIdAndBookIdAndAvailability(libraryId: Long, bookId: Long, availability: Availability): List<BookPresence>
    fun findAllByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<BookPresence>
    fun deleteBookPresenceByIdAndLibraryId(bookId: Long, libraryId: Long)
}
