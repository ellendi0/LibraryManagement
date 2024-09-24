package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Journal
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface JournalService {
    fun save(journal: Journal): Mono<Journal>
    fun getJournalById(id: String): Mono<Journal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: String, userId: String): Mono<Journal>
    fun getJournalByUserId(userId: String): Flux<Journal>
    fun deleteJournalById(journalId: String): Mono<Unit>
}
