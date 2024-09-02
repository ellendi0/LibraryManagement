package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.mongo.MongoUser.Companion.COLLECTION_NAME
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoUser(
    @Id val id: ObjectId ?= null,
    val firstName: String,
    val lastName: String,

    @Indexed(unique = true)
    val email: String,

    val password: String,

    @Indexed(unique = true)
    val phoneNumber: String
) {
    companion object{
        const val COLLECTION_NAME = "users"
    }
}
