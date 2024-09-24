package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.mongo.MongoJournal
import org.bson.types.ObjectId

object MongoJournalMapper {
    fun toEntity(journal: Journal): MongoJournal{
        return MongoJournal(
                id = journal.id?.let { ObjectId(it) },
                dateOfBorrowing = journal.dateOfBorrowing,
                dateOfReturning = journal.dateOfReturning,
                userId = ObjectId(journal.userId),
                bookPresenceId = ObjectId(journal.bookPresenceId),
        )
    }

    fun toDomain(mongoJournal: MongoJournal): Journal{
        return Journal(
                id = mongoJournal.id.toString(),
                dateOfBorrowing = mongoJournal.dateOfBorrowing,
                dateOfReturning = mongoJournal.dateOfReturning,
                userId = mongoJournal.userId.toString(),
                bookPresenceId = mongoJournal.bookPresenceId.toString()
        )
    }
}
