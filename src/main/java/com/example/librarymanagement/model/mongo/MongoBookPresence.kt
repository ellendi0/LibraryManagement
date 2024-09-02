package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.mongo.MongoBookPresence.Companion.COLLECTION_NAME
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoBookPresence (
    @Id val id: ObjectId? = null,
    var availability: Availability = Availability.AVAILABLE,
    val bookId: ObjectId?,
    val libraryId: ObjectId?,
    var userId: ObjectId? = null,
) {
    companion object{
        const val COLLECTION_NAME = "presence_of_book"
    }
}
