package com.example.librarymanagement.author.application.service

import com.example.librarymanagement.author.application.port.`in`.GetAuthorByIdInPort
import com.example.librarymanagement.author.application.port.`in`.UpdateAuthorInPort
import com.example.librarymanagement.author.application.port.out.AuthorRepositoryOutPort
import com.example.librarymanagement.author.domain.Author
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdateAuthorUseCase(
    private val authorRepository: AuthorRepositoryOutPort,
    private val getAuthorByIdInPort: GetAuthorByIdInPort
) : UpdateAuthorInPort {
    override fun updateAuthor(updatedAuthor: Author): Mono<Author> {
        return getAuthorByIdInPort.getAuthorById(updatedAuthor.id!!)
            .map { it.copy( firstName = updatedAuthor.firstName, lastName = updatedAuthor.lastName ) }
            .flatMap { authorRepository.save(it) }
    }
}
