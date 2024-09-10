package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.jpa.JpaJournal

object JpaJournalMapper {
    fun toEntity(journal: Journal): JpaJournal {
        return JpaJournal(
            id = journal.id?.toLong(),
            dateOfBorrowing = journal.dateOfBorrowing,
            dateOfReturning = journal.dateOfReturning,
            user = null,
            bookPresence = null
        )
    }

    fun toDomain(jpaJournal: JpaJournal): Journal {
        return Journal(
            id = jpaJournal.id.toString(),
            dateOfBorrowing = jpaJournal.dateOfBorrowing,
            dateOfReturning = jpaJournal.dateOfReturning,
            userId = jpaJournal.user?.id.toString(),
            bookPresenceId = jpaJournal.bookPresence?.id.toString()
        )
    }
}
