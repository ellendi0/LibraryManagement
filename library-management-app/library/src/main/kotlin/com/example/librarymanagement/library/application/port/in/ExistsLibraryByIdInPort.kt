package com.example.librarymanagement.library.application.port.`in`

import reactor.core.publisher.Mono

interface ExistsLibraryByIdInPort {
    fun existsLibraryById(id: String): Mono<Boolean>
}
