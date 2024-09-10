package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.mongo.MongoReservation
import org.bson.types.ObjectId

object MongoReservationMapper {
    fun toEntity(reservation: Reservation): MongoReservation {
        return MongoReservation(
                id = reservation.id?.let { ObjectId(it) },
                userId = ObjectId(reservation.userId),
                bookId = ObjectId(reservation.bookId),
                libraryId = reservation.libraryId?.let { ObjectId(it) },
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
