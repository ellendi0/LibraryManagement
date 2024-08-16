package com.example.librarymanagement.model.mongo

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "author")
data class MongoAuthor (
    @Id val id: ObjectId?= null,
    val firstName: String,
    val lastName: String,
)
