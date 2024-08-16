package com.example.librarymanagement.model.mongo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "publisher")
data class MongoPublisher (
    @Id val id: ObjectId? = null,
    val name: String,
)
