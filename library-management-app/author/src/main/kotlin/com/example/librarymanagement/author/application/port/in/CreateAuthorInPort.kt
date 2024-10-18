package com.example.librarymanagement.author.application.port.`in`

import com.example.librarymanagement.author.domain.Author
import reactor.core.publisher.Mono

interface CreateAuthorInPort {
    fun createAuthor(author: Author): Mono<Author>
}
