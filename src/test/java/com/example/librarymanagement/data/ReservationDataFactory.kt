package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.jpa.JpaReservation
import com.example.librarymanagement.model.mongo.MongoReservation
import org.bson.types.ObjectId

object ReservationDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")

    fun createReservation(id: String = ID): Reservation {
        return Reservation(
            id = id,
            bookId = id,
            libraryId = id,
            userId = id
        )
    }

    fun createJpaReservation(id: Long = JPA_ID): JpaReservation {
        return JpaReservation(
            id = id,
            user = null,
            book = null,
            library = null
        )
    }

    fun createMongoReservation(id: ObjectId = MONGO_ID): MongoReservation {
        return MongoReservation(
            id = id,
            bookId = id,
            libraryId = id,
            userId = id
        )
    }
}
