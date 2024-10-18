package com.example.librarymanagement.reservation.infrastructure.convertor

import com.example.internalapi.model.Reservation as ReservationProto
import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.librarymanagement.reservation.domain.Reservation
import com.example.librarymanagement.reservation.infrastructure.entity.MongoReservation
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class ReservationMapper {
    fun toReservation(request: CreateReservationRequest): Reservation {
        return Reservation(
            bookId = request.bookId,
            libraryId = request.libraryId,
            userId = request.userId
        )
    }

    fun toReservationProto(reservation: Reservation): ReservationProto {
        return com.example.internalapi.model.Reservation.newBuilder()
            .setId(reservation.id)
            .setBookId(reservation.bookId)
            .setLibraryId(reservation.libraryId)
            .setUserId(reservation.userId)
            .build()
    }

    fun toEntity(reservation: Reservation): MongoReservation {
        return MongoReservation(
            id = reservation.id?.let { ObjectId(it) },
            userId = ObjectId(reservation.userId),
            bookId = ObjectId(reservation.bookId),
            libraryId = ObjectId(reservation.libraryId)
        )
    }

    fun toDomain(mongoReservation: MongoReservation): Reservation{
        return Reservation(
            id = mongoReservation.id?.toString(),
            userId = mongoReservation.userId.toString(),
            bookId =  mongoReservation.bookId.toString(),
            libraryId = mongoReservation.libraryId.toString()
        )
    }
}
