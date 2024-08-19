package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.jpa.JpaJournal

object JpaJournalMapper {
    fun toEntity(journal: Journal): JpaJournal{
        return JpaJournal(
                id = journal.id?.toLong(),
                dateOfBorrowing = journal.dateOfBorrowing,
                dateOfReturning = journal.dateOfReturning,
                user = JpaUserMapper.toEntity(journal.user),
                bookPresence = JpaBookPresenceMapper.toEntity(journal.bookPresence)
        )
    }

    fun toDomain(jpaJournal: JpaJournal): Journal{
        return Journal(
                id = jpaJournal.id.toString(),
                dateOfBorrowing = jpaJournal.dateOfBorrowing,
                dateOfReturning = jpaJournal.dateOfReturning,
                user = JpaUserMapper.toDomain(jpaJournal.user),
                bookPresence = JpaBookPresenceMapper.toDomain(jpaJournal.bookPresence)
        )
    }
}
