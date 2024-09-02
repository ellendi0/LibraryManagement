package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.ReservationOutcome

interface ReservationService {
    fun getReservationsByUserId(userId: String): List<Reservation>
    fun reserveBook(userId: String, libraryId: String?, bookId: String): ReservationOutcome
    fun cancelReservation(userId: String, bookId: String)
    fun deleteReservationById(id: String)
}
