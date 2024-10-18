package com.example.librarymanagement.publisher.infrastructure.entity

import com.example.librarymanagement.publisher.infrastructure.entity.MongoPublisher.Companion.COLLECTION_NAME
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoPublisher(
    @Id val id: ObjectId? = null,
    val name: String,
) {
    companion object {
        const val COLLECTION_NAME = "publisher"
    }
}
