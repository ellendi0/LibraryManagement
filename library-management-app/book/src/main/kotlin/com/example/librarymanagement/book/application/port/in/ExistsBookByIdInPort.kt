package com.example.librarymanagement.book.application.port.`in`

import reactor.core.publisher.Mono

interface ExistsBookByIdInPort {
    fun existsBookById(id: String): Mono<Boolean>
}
