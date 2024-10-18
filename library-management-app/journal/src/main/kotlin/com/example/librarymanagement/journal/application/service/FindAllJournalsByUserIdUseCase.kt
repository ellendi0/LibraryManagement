package com.example.librarymanagement.journal.application.service

import com.example.librarymanagement.journal.application.port.`in`.FindAllJournalsByUserIdInPort
import com.example.librarymanagement.journal.application.port.out.JournalRepositoryOutPort
import com.example.librarymanagement.journal.domain.Journal
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllJournalsByUserIdUseCase(private val journalRepository: JournalRepositoryOutPort): FindAllJournalsByUserIdInPort {
    override fun findAllJournalsByUserId(userId: String): Flux<Journal> = journalRepository.findAllByUserId(userId)
}
