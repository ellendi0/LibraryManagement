package com.example.librarymanagement.library.infrastructure.entity

import com.example.librarymanagement.library.infrastructure.entity.MongoLibrary.Companion.COLLECTION_NAME
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
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
