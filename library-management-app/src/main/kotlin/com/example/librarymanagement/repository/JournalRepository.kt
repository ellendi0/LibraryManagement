package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Journal
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface JournalRepository {
    fun saveOrUpdate(journal: Journal): Mono<Journal>
    fun findById(journalId: String): Mono<Journal>
    fun deleteById(journalId: String): Mono<Unit>
    fun findAllByUserId(userId: String): Flux<Journal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: String, userId: String): Mono<Journal>
}
