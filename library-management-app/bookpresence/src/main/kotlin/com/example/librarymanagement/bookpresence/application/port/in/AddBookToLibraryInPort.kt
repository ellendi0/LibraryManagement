package com.example.librarymanagement.bookpresence.application.port.`in`

import com.example.librarymanagement.bookpresence.domain.BookPresence
import reactor.core.publisher.Mono

interface AddBookToLibraryInPort {
    fun addBookToLibrary(libraryId: String, bookId: String): Mono<BookPresence>
}
