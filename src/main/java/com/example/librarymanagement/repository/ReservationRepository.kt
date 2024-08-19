package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.User

interface ReservationRepository {
    fun save(reservation: Reservation): Reservation
    fun findById(reservationId: String): Reservation?
    fun deleteById(reservationId: String)
    fun delete(reservation: Reservation)
    fun reserveBook(user: User, libraryId: String?, bookId: String): List<Reservation>
    fun findAllByBookIdAndUserId(bookId: String, userId: String): List<Reservation>
    fun findAllByLibraryId(libraryId: String): List<Reservation>
    fun findAllByUserId(userId: String): List<Reservation>
    fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: String, libraryId: String?): Reservation?
    fun existsByBookIdAndUser(bookId: String, user: User): Boolean
}
