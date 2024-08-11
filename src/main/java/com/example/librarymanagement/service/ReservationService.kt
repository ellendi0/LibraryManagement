package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User

interface ReservationService {
    fun getReservationsByLibraryId(libraryId: Long): List<Reservation>
    fun getReservationsByUserId(userId: Long): List<Reservation>
    fun getReservationsByBookIdAndUser(bookId: Long, user: User): List<Reservation>
    fun reserveBook(user: User, libraryId: Long?, bookId: Long): List<Reservation>
    fun removeReservation(user: User, bookId: Long)
    fun deleteReservationById(id: Long)
}
