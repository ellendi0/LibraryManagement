package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Book
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BookService {
    fun getAll(): Flux<Book>
    fun getBookById(id: String): Mono<Book>
    fun getBookByTitleAndAuthor(title: String, authorId: String): Flux<Book>
    fun createBook(book: Book): Mono<Book>
    fun updateBook(updatedBook: Book): Mono<Book>
    fun deleteBookById(id: String): Mono<Unit>
    fun existsBookById(id: String): Mono<Boolean>
}
