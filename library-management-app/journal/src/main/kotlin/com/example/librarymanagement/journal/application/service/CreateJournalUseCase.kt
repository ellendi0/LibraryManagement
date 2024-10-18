package com.example.librarymanagement.journal.application.service

import com.example.librarymanagement.journal.application.port.`in`.CreateJournalInPort
import com.example.librarymanagement.journal.application.port.out.JournalRepositoryOutPort
import com.example.librarymanagement.journal.domain.Journal
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateJournalUseCase(private val journalRepository: JournalRepositoryOutPort): CreateJournalInPort {
    override fun createJournal(journal: Journal): Mono<Journal> = journalRepository.saveOrUpdate(journal)
}
