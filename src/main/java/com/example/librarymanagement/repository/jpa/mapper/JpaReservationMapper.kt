package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.jpa.JpaReservation

object JpaReservationMapper {
    fun toEntity(reservation: Reservation): JpaReservation{
        return JpaReservation(
                id = reservation.id?.toLong(),
                user = JpaUserMapper.toEntity(reservation.user),
                book = JpaBookMapper.toEntity(reservation.book),
                library = reservation.library?.let { JpaLibraryMapper.toEntity(it) }
        )
    }

    fun toDomain(jpaReservation: JpaReservation): Reservation{
        return Reservation(
                id = jpaReservation.id?.toString(),
                user = JpaUserMapper.toDomain(jpaReservation.user),
                book = JpaBookMapper.toDomain(jpaReservation.book),
                library = jpaReservation.library?.let { JpaLibraryMapper.toDomain(it) }
        )
    }
}
