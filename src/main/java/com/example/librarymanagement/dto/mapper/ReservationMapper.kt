package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.ReservationDto
import com.example.librarymanagement.model.domain.Reservation
import org.springframework.stereotype.Component

@Component
class ReservationMapper {
    fun toReservationDto(reservation: Reservation): ReservationDto {
        val book = reservation.book
        val author = book.author!!

        return ReservationDto(
            id = reservation.id!!,
            bookTitle = book.title,
            author = "${author.firstName} ${author.lastName}",
            nameOfLibrary = reservation.library?.name
        )
    }

    fun toReservationDto(reservations: List<Reservation>): List<ReservationDto> {
        return reservations.map { toReservationDto(it) }
    }
}
