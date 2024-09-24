package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Library
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface LibraryRepository{
    fun save(library: Library): Mono<Library>
    fun findById(libraryId: String): Mono<Library>
    fun findAll(): Flux<Library>
    fun deleteById(libraryId: String): Mono<Unit>
    fun existsById(libraryId: String): Mono<Boolean>
}
