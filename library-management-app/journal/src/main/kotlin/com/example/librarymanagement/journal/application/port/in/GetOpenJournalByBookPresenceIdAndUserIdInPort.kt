package com.example.librarymanagement.journal.application.port.`in`

import com.example.librarymanagement.journal.domain.Journal
import reactor.core.publisher.Mono

interface GetOpenJournalByBookPresenceIdAndUserIdInPort {
    fun getOpenJournalByBookPresenceIdAndUserId(bookPresenceId: String, userId: String): Mono<Journal>
}
