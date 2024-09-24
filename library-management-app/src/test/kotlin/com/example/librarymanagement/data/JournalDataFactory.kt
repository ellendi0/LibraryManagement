package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.jpa.JpaJournal
import com.example.librarymanagement.model.mongo.MongoJournal
import org.bson.types.ObjectId
import java.time.LocalDate

object JournalDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")

    fun createJournal(id: String = ID): Journal {
        return Journal(
            id = id,
            bookPresenceId = id,
            userId = id,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7),
        )
    }

    fun createJpaJournal(id: Long = JPA_ID): JpaJournal {
        return JpaJournal(
            id = id,
            bookPresence = null,
            user = null,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7),
        )
    }

    fun createMongoJournal(id: ObjectId = MONGO_ID): MongoJournal {
        return MongoJournal(
            id = id,
            bookPresenceId = id,
            userId = id,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7),
        )
    }
}
