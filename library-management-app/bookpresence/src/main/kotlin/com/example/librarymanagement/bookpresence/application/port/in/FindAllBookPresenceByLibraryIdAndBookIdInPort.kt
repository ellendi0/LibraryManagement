package com.example.librarymanagement.bookpresence.application.port.`in`

import com.example.librarymanagement.bookpresence.domain.BookPresence
import reactor.core.publisher.Flux

interface FindAllBookPresenceByLibraryIdAndBookIdInPort {
    fun findAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence>
}
