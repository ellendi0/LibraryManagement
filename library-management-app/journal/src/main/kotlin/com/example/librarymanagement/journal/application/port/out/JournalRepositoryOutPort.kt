package com.example.librarymanagement.journal.application.port.out

import com.example.librarymanagement.journal.domain.Journal
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface JournalRepositoryOutPort {
    fun saveOrUpdate(journal: Journal): Mono<Journal>
    fun findById(journalId: String): Mono<Journal>
    fun deleteById(journalId: String): Mono<Unit>
    fun findAllByUserId(userId: String): Flux<Journal>
    fun findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId: String, userId: String): Mono<Journal>
}
