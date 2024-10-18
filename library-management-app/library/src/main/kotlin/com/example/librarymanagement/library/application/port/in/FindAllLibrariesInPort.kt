package com.example.librarymanagement.library.application.port.`in`

import com.example.librarymanagement.library.domain.Library
import reactor.core.publisher.Flux

interface FindAllLibrariesInPort {
    fun findAllLibraries(): Flux<Library>
}
