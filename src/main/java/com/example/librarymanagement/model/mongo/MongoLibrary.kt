package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.mongo.MongoLibrary.Companion.COLLECTION_NAME
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoLibrary (
    @Id val id: ObjectId? = null,
    val name: String,
    val address: String,
) {
    companion object{
        const val COLLECTION_NAME = "library"
    }
}
