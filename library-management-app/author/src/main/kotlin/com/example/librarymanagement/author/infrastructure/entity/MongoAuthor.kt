package com.example.librarymanagement.author.infrastructure.entity

import com.example.librarymanagement.author.infrastructure.entity.MongoAuthor.Companion.COLLECTION_NAME
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoAuthor(
    @Id val id: ObjectId? = null,
    val firstName: String,
    val lastName: String
) {
    companion object {
        const val COLLECTION_NAME = "author"
    }
}
