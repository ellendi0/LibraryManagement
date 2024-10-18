package com.example.librarymanagement.library.application.port.`in`

import reactor.core.publisher.Mono

interface DeleteLibraryByIdInPort {
    fun deleteLibraryById(id: String): Mono<Unit>
}
