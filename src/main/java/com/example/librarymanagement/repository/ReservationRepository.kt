package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Reservation

interface ReservationRepository {
    fun save(reservation: Reservation): Reservation
    fun findById(reservationId: String): Reservation?
    fun deleteById(reservationId: String)
    fun findAllByBookIdAndUserId(bookId: String, userId: String): List<Reservation>
    fun findAllByLibraryId(libraryId: String): List<Reservation>
    fun findAllByUserId(userId: String): List<Reservation>
    fun findAllByBookId(bookId: String): List<Reservation>
    fun findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId: String, libraryId: String?): Reservation?
    fun existsByBookIdAndUserId(bookId: String, userId: String): Boolean
    fun findAllByBookIdAndLibraryId(bookId: String, libraryId: String): List<Reservation>
}
