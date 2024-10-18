package com.example.librarymanagement.author.application.port.out

import com.example.librarymanagement.author.domain.Author
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AuthorRepositoryOutPort {
    fun save(author: Author): Mono<Author>
    fun findById(authorId: String): Mono<Author>
    fun findAll(): Flux<Author>
}
