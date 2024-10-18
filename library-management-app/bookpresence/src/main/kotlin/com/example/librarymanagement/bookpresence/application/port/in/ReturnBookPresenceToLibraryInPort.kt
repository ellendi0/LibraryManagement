package com.example.librarymanagement.bookpresence.application.port.`in`

import com.example.librarymanagement.journal.domain.Journal
import reactor.core.publisher.Flux

interface ReturnBookPresenceToLibraryInPort {
    fun returnBookToLibrary(userId: String, libraryId: String, bookId: String): Flux<Journal>
}
