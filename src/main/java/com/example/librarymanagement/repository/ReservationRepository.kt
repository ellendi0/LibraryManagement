package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<Reservation, Long> {
    fun findAllByBookIdAndUserId(bookId: Long, userId: Long): List<Reservation>
    fun findAllByLibraryId(libraryId: Long): List<Reservation>
    fun findAllByUserId(userId: Long): List<Reservation>
    fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: Long, libraryId: Long?): Reservation?
    fun existsByBookIdAndUser(bookId: Long, user: User): Boolean
}
