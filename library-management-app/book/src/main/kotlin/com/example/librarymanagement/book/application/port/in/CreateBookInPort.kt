package com.example.librarymanagement.book.application.port.`in`

import com.example.librarymanagement.book.domain.Book
import reactor.core.publisher.Mono

interface CreateBookInPort {
    fun createBook(book: Book): Mono<Book>
}
