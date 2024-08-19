package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.User

interface ReservationService {
    fun getReservationsByUserId(userId: String): List<Reservation>
    fun reserveBook(user: User, libraryId: String?, bookId: String): List<Reservation>
    fun removeReservation(user: User, bookId: String)
    fun deleteReservationById(id: String)
}
