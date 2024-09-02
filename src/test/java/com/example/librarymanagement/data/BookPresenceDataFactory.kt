package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBookPresence
import com.example.librarymanagement.model.mongo.MongoBookPresence
import org.bson.types.ObjectId

object BookPresenceDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")

    fun createBookPresence(id: Any): BookPresence {
        return BookPresence(
            id = id.toString(),
            availability = Availability.UNAVAILABLE,
            bookId = id.toString(),
            libraryId = id.toString(),
            userId = id.toString()
        )
    }

    fun createJpaBookPresence(): JpaBookPresence {
        return JpaBookPresence(
            id = JPA_ID,
            availability = Availability.UNAVAILABLE,
            book = null,
            library = null,
            user = null
        )
    }

    fun createMongoBookPresence(): MongoBookPresence {
        return MongoBookPresence(
            id = MONGO_ID,
            availability = Availability.UNAVAILABLE,
            bookId = MONGO_ID,
            libraryId = MONGO_ID,
            userId = MONGO_ID
        )
    }
}
