package com.example.librarymanagement.model.mongo

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document(collection = "library")
data class MongoLibrary (
    @Id val id: ObjectId ?= null,
    val name: String,
    val address: String,

    @DocumentReference
    val bookPresence: MutableList<MongoBookPresence> = mutableListOf()
)
