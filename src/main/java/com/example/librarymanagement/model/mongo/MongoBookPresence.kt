package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.enums.Availability
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document(collection = "presence_of_book")
data class MongoBookPresence (
    @Id val id: ObjectId? = null,
    var availability: Availability = Availability.AVAILABLE,
    val book: MongoBook,
    val library: MongoLibrary,
    var user: MongoUser? = null,

    @DocumentReference
    val journals: MutableList<MongoJournal> = mutableListOf()
)
