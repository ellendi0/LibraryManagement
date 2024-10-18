package com.example.librarymanagement.author.application.service

import com.example.librarymanagement.author.application.port.`in`.GetAuthorByIdInPort
import com.example.librarymanagement.author.application.port.out.AuthorRepositoryOutPort
import com.example.librarymanagement.author.domain.Author
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GetAuthorByIdUseCase(
    private val authorRepository: AuthorRepositoryOutPort
) : GetAuthorByIdInPort {
    override fun getAuthorById(id: String): Mono<Author> {
        return authorRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Author")))
    }
}
