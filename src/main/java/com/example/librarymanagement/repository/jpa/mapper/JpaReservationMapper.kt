package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.jpa.JpaReservation

object JpaReservationMapper {
    fun toEntity(reservation: Reservation): JpaReservation {
        return JpaReservation(
            id = reservation.id?.toLong(),
            user = null,
            book = null,
            library = null
        )
    }

    fun toDomain(jpaReservation: JpaReservation): Reservation {
        return Reservation(
            id = jpaReservation.id?.toString(),
            userId = jpaReservation.user?.id.toString(),
            bookId = jpaReservation.book?.id.toString(),
            libraryId = jpaReservation.library?.id.toString()
        )
    }
}
