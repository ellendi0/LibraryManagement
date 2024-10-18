package com.example.librarymanagement.book.application.port.`in`

import com.example.librarymanagement.book.domain.Book
import reactor.core.publisher.Mono

interface GetByIdBookInPort {
    fun getBookById(id: String): Mono<Book>
}
