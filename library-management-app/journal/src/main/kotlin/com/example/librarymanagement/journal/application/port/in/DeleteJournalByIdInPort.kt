package com.example.librarymanagement.journal.application.port.`in`

import reactor.core.publisher.Mono

interface DeleteJournalByIdInPort {
    fun deleteJournalById(journalId: String): Mono<Unit>
}
