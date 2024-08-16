package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.jpa.JpaLibrary
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "reservations")
class MongoReservation {
    @Id val id: ObjectId ?= null
    val user: MongoUser?= null
    val book: MongoBook?= null
    val library: JpaLibrary?= null
}
