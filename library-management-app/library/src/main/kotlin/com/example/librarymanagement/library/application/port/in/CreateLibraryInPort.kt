package com.example.librarymanagement.library.application.port.`in`

import com.example.librarymanagement.library.domain.Library
import reactor.core.publisher.Mono

interface CreateLibraryInPort {
    fun createLibrary(library: Library): Mono<Library>
}
