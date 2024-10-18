package com.example.librarymanagement.author.application.port.`in`

import com.example.librarymanagement.author.domain.Author
import reactor.core.publisher.Flux

interface FindAllAuthorsInPort {
    fun findAllAuthors(): Flux<Author>
}
