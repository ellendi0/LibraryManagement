package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.mongo.MongoReservation.Companion.COLLECTION_NAME
import jakarta.persistence.Id
import org.bson.types.ObjectId
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
