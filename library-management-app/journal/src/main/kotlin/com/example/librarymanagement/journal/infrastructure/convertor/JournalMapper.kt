package com.example.librarymanagement.journal.infrastructure.convertor

import com.example.librarymanagement.journal.domain.Journal
import com.example.internalapi.model.Journal as JournalProto
import com.example.librarymanagement.journal.infrastructure.entity.MongoJournal
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class JournalMapper {
    fun toJournalProto(journal: Journal): JournalProto {
        return JournalProto.newBuilder()
            .setId(journal.id)
            .setDateOfBorrowing(journal.dateOfBorrowing.toString())
            .setDateOfReturning(journal.dateOfReturning.toString())
            .setUserId(journal.userId)
            .setBookPresenceId(journal.bookPresenceId)
            .build()
    }

    fun toEntity(journal: Journal): MongoJournal {
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
