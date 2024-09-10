package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.jpa.JpaBookPresence
import com.example.librarymanagement.model.mongo.MongoBookPresence
import org.bson.types.ObjectId

object BookPresenceDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")

    fun createBookPresence(id: String = ID): BookPresence {
        return BookPresence(
            id = id,
            availability = Availability.UNAVAILABLE,
            bookId = id,
            libraryId = id,
            userId = id
        )
    }

    fun createJpaBookPresence(id: Long = JPA_ID): JpaBookPresence {
        return JpaBookPresence(
            id = id,
            availability = Availability.UNAVAILABLE,
            book = null,
            library = null,
            user = null
        )
    }

    fun createMongoBookPresence(id: ObjectId = MONGO_ID): MongoBookPresence {
        return MongoBookPresence(
            id = id,
            availability = Availability.UNAVAILABLE,
            bookId = id,
            libraryId = id,
            userId = id
        )
    }
}
