package com.example.librarymanagement.author.application.port.`in`

import com.example.librarymanagement.author.domain.Author
import reactor.core.publisher.Mono

interface UpdateAuthorInPort {
    fun updateAuthor(updatedAuthor: Author): Mono<Author>
}
