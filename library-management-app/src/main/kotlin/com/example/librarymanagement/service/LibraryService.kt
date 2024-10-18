package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Library
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface LibraryService {
    fun getAllLibraries(): Flux<Library>
    fun getLibraryById(id: String): Mono<Library>
    fun createLibrary(library: Library): Mono<Library>
    fun updateLibrary(updatedLibrary: Library): Mono<Library>
    fun deleteLibraryById(id: String): Mono<Unit>
    fun existsLibraryById(id: String): Mono<Boolean>
}
