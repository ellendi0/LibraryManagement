package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.enums.Genre
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document(collection = "book")
data class MongoBook (
    @Id val id: ObjectId? = null,
    val title: String,
    val author: MongoAuthor? = null,
    val publisher: MongoPublisher?= null,
    val publishedYear: Int,

    @Indexed(unique = true)
    val isbn: Long,
    val genre: Genre,

    @DocumentReference
    val bookPresence: MutableList<MongoBookPresence> = mutableListOf(),
    @DocumentReference
    val reservations: MutableList<MongoReservation> = mutableListOf()
)
