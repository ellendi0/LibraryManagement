package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.repository.AuthorRepository
import com.example.librarymanagement.service.AuthorService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {

    override fun createAuthor(author: Author): Mono<Author> = authorRepository.save(author)

    override fun updateAuthor(updatedAuthor: Author): Mono<Author> {
        return getAuthorById(updatedAuthor.id!!)
            .map { it.copy( firstName = updatedAuthor.firstName, lastName = updatedAuthor.lastName ) }
            .flatMap { authorRepository.save(it) }
    }

    override fun getAuthorById(id: String): Mono<Author> {
        return authorRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Author")))
    }

    override fun getAllAuthors(): Flux<Author> = authorRepository.findAll()
}
