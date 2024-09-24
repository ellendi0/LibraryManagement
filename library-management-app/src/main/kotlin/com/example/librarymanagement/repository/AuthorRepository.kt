package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Author
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AuthorRepository{
    fun save(author: Author): Mono<Author>
    fun findById(authorId: String): Mono<Author>
    fun findAll(): Flux<Author>
}
