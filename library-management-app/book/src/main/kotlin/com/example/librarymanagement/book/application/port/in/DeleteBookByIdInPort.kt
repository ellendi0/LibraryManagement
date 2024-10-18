package com.example.librarymanagement.book.application.port.`in`

import reactor.core.publisher.Mono

interface DeleteBookByIdInPort {
    fun deleteBookById(id: String): Mono<Unit>
}
