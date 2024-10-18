package com.example.librarymanagement.book.application.port.`in`

import com.example.librarymanagement.book.domain.Book
import reactor.core.publisher.Flux

interface FindAllBooksInPort {
    fun findAll(): Flux<Book>
}
