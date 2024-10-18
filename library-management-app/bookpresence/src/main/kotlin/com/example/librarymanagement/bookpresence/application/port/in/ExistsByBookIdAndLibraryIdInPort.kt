package com.example.librarymanagement.bookpresence.application.port.`in`

import reactor.core.publisher.Mono

interface ExistsByBookIdAndLibraryIdInPort {
    fun existsBookPresenceByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean>
}
