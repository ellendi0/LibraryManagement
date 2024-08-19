package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Journal

interface JournalService {
    fun createJournal(journal: Journal): Journal
    fun getJournalById(id: String): Journal
    fun updateJournal(id: String, updatedJournal: Journal): Journal
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: String, userId: String): Journal
    fun getJournalByUserId(userId: String): List<Journal>
}
