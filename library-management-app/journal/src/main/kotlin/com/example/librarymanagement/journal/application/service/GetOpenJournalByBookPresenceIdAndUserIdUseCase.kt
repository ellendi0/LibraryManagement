package com.example.librarymanagement.journal.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.journal.application.port.`in`.GetOpenJournalByBookPresenceIdAndUserIdInPort
import com.example.librarymanagement.journal.application.port.out.JournalRepositoryOutPort
import com.example.librarymanagement.journal.domain.Journal
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class GetOpenJournalByBookPresenceIdAndUserIdUseCase(
    private val journalRepository: JournalRepositoryOutPort
) : GetOpenJournalByBookPresenceIdAndUserIdInPort {
    override fun getOpenJournalByBookPresenceIdAndUserId(bookPresenceId: String, userId: String): Mono<Journal> {
        return journalRepository
            .findByBookPresenceIdAndUserIdAndDateOfReturningIsNull(bookPresenceId, userId)
            .switchIfEmpty { Mono.error(EntityNotFoundException("Journal")) }
    }
}
