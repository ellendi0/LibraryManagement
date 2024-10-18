package com.example.librarymanagement.library.application.port.out

import com.example.librarymanagement.library.domain.Library
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface LibraryRepositoryOutPort {
    fun save(library: Library): Mono<Library>
    fun findById(libraryId: String): Mono<Library>
    fun findAll(): Flux<Library>
    fun deleteById(libraryId: String): Mono<Unit>
    fun existsById(libraryId: String): Mono<Boolean>
    fun existsLibraryWithAvailableBook(bookId: String, libraryId: String): Mono<Boolean>
}
