package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.jpa.JpaReservation
import com.example.librarymanagement.model.mongo.MongoReservation
import org.bson.types.ObjectId

object ReservationDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")

    fun createReservation(id: Any): Reservation {
        return Reservation(
            id = id.toString(),
            bookId = id.toString(),
            libraryId = id.toString(),
            userId = id.toString()
        )
    }

    fun createJpaReservation(): JpaReservation {
        return JpaReservation(
            id = JPA_ID,
            user = null,
            book = null,
            library = null
        )
    }
    //id
    fun createMongoReservation(/*id: String = Rand*/): MongoReservation {
        return MongoReservation(
            id = MONGO_ID,
            bookId = MONGO_ID,
            libraryId = MONGO_ID,
            userId = MONGO_ID
        )
    }
}
