package com.example.librarymanagement.journal.application.port.`in`

import com.example.librarymanagement.journal.domain.Journal
import reactor.core.publisher.Flux

interface FindAllJournalsByUserIdInPort {
    fun findAllJournalsByUserId(userId: String): Flux<Journal>
}
