package com.example.librarymanagement.model.mongo

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "journal")
class MongoJournal (
    @Id val id: ObjectId ?= null,
    var dateOfBorrowing: LocalDate,
    var dateOfReturning: LocalDate?= null,
    var user: MongoUser?= null,
    val bookPresence: MongoBookPresence?= null,
)
