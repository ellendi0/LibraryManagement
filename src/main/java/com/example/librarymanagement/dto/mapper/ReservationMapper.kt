package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.ReservationDto
import com.example.librarymanagement.model.domain.Reservation
import org.springframework.stereotype.Component

@Component
class ReservationMapper {
    fun toReservationDto(reservation: Reservation): ReservationDto {
        return ReservationDto(
            id = reservation.id,
            bookId = reservation.bookId,
            userId = reservation.userId,
            libraryId = reservation.libraryId
        )
    }

    fun toReservationDto(reservations: List<Reservation>): List<ReservationDto> {
        return reservations.map { toReservationDto(it) }
    }
}
