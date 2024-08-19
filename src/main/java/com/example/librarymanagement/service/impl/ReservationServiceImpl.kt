package com.example.librarymanagement.service.impl

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.ReservationService
import org.springframework.stereotype.Service

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationService {

    override fun getReservationsByUserId(userId: String): List<Reservation> {
        return reservationRepository.findAllByUserId(userId)
    }

    override fun reserveBook(user: User, libraryId: String?, bookId: String): List<Reservation> {
        return reservationRepository.reserveBook(user, libraryId, bookId)
    }

    override fun removeReservation(user: User, bookId: String) {
        reservationRepository.findAllByBookIdAndUserId(bookId, user.id!!)
            .forEach { deleteReservationById(it.id.toString()) }
    }

    override fun deleteReservationById(id: String) {
        reservationRepository.findById(id)?.let { reservationRepository.delete(it) }
    }
}
