package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.mongo.MongoAuthor.Companion.COLLECTION_NAME
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoAuthor(
    @Id val id: ObjectId? = null,
    val firstName: String,
    val lastName: String
) {
    companion object{
        const val COLLECTION_NAME = "author"
    }
}
