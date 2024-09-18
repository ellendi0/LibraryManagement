package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Author
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AuthorService {
    fun createAuthor(author: Author): Mono<Author>
    fun updateAuthor(updatedAuthor: Author): Mono<Author>
    fun getAuthorById(id: String): Mono<Author>
    fun getAllAuthors(): Flux<Author>
}
