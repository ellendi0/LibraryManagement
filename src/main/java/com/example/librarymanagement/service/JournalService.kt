package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.Journal

interface JournalService {
    fun createJournal(journal: Journal): Journal
    fun getJournalById(id: Long): Journal
    fun updateJournal(id: Long, updatedJournal: Journal): Journal
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: Long, userId: Long): Journal
    fun getJournalByBookPresenceIdAndUserId(bookPresenceId: Long, userId: Long): List<Journal>
    fun getJournalByUserId(userId: Long): List<Journal>
    fun deleteJournal(id: Long)
}