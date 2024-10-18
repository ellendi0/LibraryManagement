package com.example.librarymanagement.author.application.service

import com.example.librarymanagement.author.application.port.`in`.CreateAuthorInPort
import com.example.librarymanagement.author.application.port.out.AuthorRepositoryOutPort
import com.example.librarymanagement.author.domain.Author
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateAuthorUseCase(
    private val authorRepository: AuthorRepositoryOutPort
) : CreateAuthorInPort {
    override fun createAuthor(author: Author): Mono<Author> = authorRepository.save(author)
}
