package com.example.librarymanagement.journal.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.journal.application.port.`in`.GetJournalByIdInPort
import com.example.librarymanagement.journal.application.port.out.JournalRepositoryOutPort
import com.example.librarymanagement.journal.domain.Journal
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class GetJournalByIdUseCase(private val journalRepository: JournalRepositoryOutPort) : GetJournalByIdInPort {
    override fun getJournalById(id: String): Mono<Journal> {
        return journalRepository
            .findById(id)
            .switchIfEmpty { Mono.error(EntityNotFoundException("Journal")) }
    }
}
