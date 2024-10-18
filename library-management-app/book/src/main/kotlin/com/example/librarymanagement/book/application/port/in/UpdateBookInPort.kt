package com.example.librarymanagement.book.application.port.`in`

import com.example.librarymanagement.book.domain.Book
import reactor.core.publisher.Mono

interface UpdateBookInPort {
    fun updateBook(updatedBook: Book): Mono<Book>
}
