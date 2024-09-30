package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.repository.JournalRepository
import com.example.librarymanagement.service.JournalService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class JournalServiceImpl(private val journalRepository: JournalRepository) : JournalService {
    override fun save(journal: Journal): Mono<Journal> = journalRepository.saveOrUpdate(journal)

    override fun getJournalById(id: String): Mono<Journal> {
        return journalRepository
            .findById(id)
            .switchIfEmpty { Mono.error(EntityNotFoundException("Journal")) }
    }

    override fun getByBookPresenceIdAndUserIdAndDateOfReturningIsNull(
        bookPresenceId: String,
        userId: String
    ): Mono<Journal> {
        return journalRepository
            .findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId, userId)
            .switchIfEmpty{ Mono.error(EntityNotFoundException("Journal")) }
    }

    override fun getJournalByUserId(userId: String): Flux<Journal> = journalRepository.findAllByUserId(userId)

    override fun deleteJournalById(journalId: String) = journalRepository.deleteById(journalId)
}
