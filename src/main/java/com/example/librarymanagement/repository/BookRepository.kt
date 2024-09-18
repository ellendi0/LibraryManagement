package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Book
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BookRepository{
    fun save(book: Book): Mono<Book>
    fun findById(bookId: String): Mono<Book>
    fun findAll(): Flux<Book>
    fun deleteById(bookId: String): Mono<Unit>
    fun existsById(bookId: String): Mono<Boolean>
    fun existsByIsbn(isbn: Long): Mono<Boolean>
    fun findBookByTitleAndAuthorId(title: String, authorId: String): Flux<Book>
}
