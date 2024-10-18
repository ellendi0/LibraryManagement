package com.example.librarymanagement.journal.application.service

import com.example.librarymanagement.journal.application.port.`in`.DeleteJournalByIdInPort
import com.example.librarymanagement.journal.application.port.out.JournalRepositoryOutPort
import org.springframework.stereotype.Service

@Service
class DeleteJournalByIdUseCase(private val journalRepository: JournalRepositoryOutPort): DeleteJournalByIdInPort {
    override fun deleteJournalById(journalId: String) = journalRepository.deleteById(journalId)
}
