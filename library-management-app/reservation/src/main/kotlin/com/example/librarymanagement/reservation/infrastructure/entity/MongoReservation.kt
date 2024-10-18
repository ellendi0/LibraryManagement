package com.example.librarymanagement.reservation.infrastructure.entity

import com.example.librarymanagement.reservation.infrastructure.entity.MongoReservation.Companion.COLLECTION_NAME
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoReservation (
    @Id val id: ObjectId? = null,
    val userId: ObjectId?,
    val bookId: ObjectId?,
    val libraryId: ObjectId?
) {
    companion object{
        const val COLLECTION_NAME = "reservation"
    }
}
