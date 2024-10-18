package com.example.librarymanagement.user.infrastructure.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import com.example.librarymanagement.user.infrastructure.entity.MongoUser.Companion.COLLECTION_NAME

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
    companion object {
        const val COLLECTION_NAME = "users"
    }
}
