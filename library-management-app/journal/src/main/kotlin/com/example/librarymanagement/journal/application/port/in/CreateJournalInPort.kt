package com.example.librarymanagement.journal.application.port.`in`

import com.example.librarymanagement.journal.domain.Journal
import reactor.core.publisher.Mono

interface CreateJournalInPort {
    fun createJournal(journal: Journal): Mono<Journal>
}
