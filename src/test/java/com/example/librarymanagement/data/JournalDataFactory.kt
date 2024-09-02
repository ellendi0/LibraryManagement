package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.model.mongo.MongoJournal
import org.bson.types.ObjectId
import java.time.LocalDate

object JournalDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")

    fun createJournal(id: Any): Journal {
        return Journal(
            id = id.toString(),
            bookPresenceId = id.toString(),
            userId = id.toString(),
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7),
        )
    }

    fun createJpaJournal(): JpaJournal {
        return JpaJournal(
            id = JPA_ID,
            bookPresence = null,
            user = null,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7),
        )
    }

    fun createMongoJournal(): MongoJournal {
        return MongoJournal(
            id = MONGO_ID,
            bookPresenceId = MONGO_ID,
            userId = MONGO_ID,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7),
        )
    }
}
