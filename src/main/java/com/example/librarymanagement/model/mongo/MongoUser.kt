package com.example.librarymanagement.model.mongo

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document
data class MongoUser(
    @Id val id: ObjectId ?= null,
    val firstName: String,
    val lastName: String,

    @Indexed(unique = true)
    val email: String,

    val password: String,

    @Indexed(unique = true)
    val phoneNumber: String,

    @DocumentReference
    val journals: MutableList<MongoJournal> = mutableListOf(),
    @DocumentReference
    val reservations: MutableList<MongoReservation> = mutableListOf(),
)
