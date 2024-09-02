package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Journal

interface JournalRepository {
    fun save(journal: Journal): Journal
    fun findById(journalId: String): Journal?
    fun deleteById(journalId: String)
    fun findAllByUserId(userId: String): List<Journal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: String, userId: String): Journal?
}
