package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.service.JournalService
import org.springframework.stereotype.Service

@Service
class JournalServiceImpl(private val journalRepository: JournalRepository) : JournalService {
    override fun createJournal(journal: Journal): Journal = journalRepository.save(journal)

    override fun getJournalById(id: Long): Journal {
        return journalRepository.findById(id).orElseThrow { throw EntityNotFoundException("Journal") }
    }

    override fun updateJournal(id: Long, updatedJournal: Journal): Journal {
        val journal = getJournalById(id).apply { this.dateOfReturning = updatedJournal.dateOfReturning }
        return journalRepository.save(journal)
    }

    override fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: Long, userId: Long): Journal {
        return journalRepository.findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId, userId)
            ?: throw EntityNotFoundException("Journal")
    }

    override fun getJournalByBookPresenceIdAndUserId(bookPresenceId: Long, userId: Long): List<Journal> {
        return journalRepository.findByBookPresenceIdAndUserId(bookPresenceId, userId)
    }

    override fun getJournalByUserId(userId: Long): List<Journal> = journalRepository.findAllByUserId(userId)

    override fun deleteJournal(id: Long) = journalRepository.deleteById(id)
}