package com.example.librarymanagement.library.application.port.`in`

import reactor.core.publisher.Mono

interface ExistsLibraryWithAvailableBookInPort {
    fun existsLibraryWithAvailableBook(bookId: String, libraryId: String): Mono<Boolean>
}
