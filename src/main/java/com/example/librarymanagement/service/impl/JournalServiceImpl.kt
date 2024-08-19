package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.service.JournalService
import org.springframework.stereotype.Service

@Service
class JournalServiceImpl(private val journalRepository: JournalRepository) : JournalService {
    override fun createJournal(journal: Journal): Journal = journalRepository.save(journal)

    override fun getJournalById(id: String): Journal {
        return journalRepository.findById(id) ?: throw EntityNotFoundException("Journal")
    }

    override fun updateJournal(id: String, updatedJournal: Journal): Journal {
        val journal = getJournalById(id).apply { this.dateOfReturning = updatedJournal.dateOfReturning }
        return journalRepository.save(journal)
    }

    override fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: String, userId: String): Journal {
        return journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId, userId)
            ?: throw EntityNotFoundException("Journal")
    }

    override fun getJournalByUserId(userId: String): List<Journal> = journalRepository.findAllByUserId(userId)
}
