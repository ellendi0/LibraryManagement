package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.librarymanagement.model.domain.Reservation
import org.springframework.stereotype.Component

@Component("natsReservationMapper")
class ReservationMapper {
    fun toReservation(request: CreateReservationRequest): Reservation {
        return Reservation(
            bookId = request.bookId,
            libraryId = request.libraryId,
            userId = request.userId
        )
    }

    fun toReservationProto(reservation: Reservation): com.example.internalapi.model.Reservation {
        return com.example.internalapi.model.Reservation.newBuilder()
            .setId(reservation.id)
            .setBookId(reservation.bookId)
            .setLibraryId(reservation.libraryId)
            .setUserId(reservation.userId)
            .build()
    }
}
