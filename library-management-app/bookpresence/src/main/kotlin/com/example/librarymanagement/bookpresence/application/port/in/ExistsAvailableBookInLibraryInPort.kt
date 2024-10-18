package com.example.librarymanagement.bookpresence.application.port.`in`

import reactor.core.publisher.Mono

interface ExistsAvailableBookInLibraryInPort {
    fun existsAvailableBookInLibrary(bookId: String, libraryId: String): Mono<Boolean>
}
