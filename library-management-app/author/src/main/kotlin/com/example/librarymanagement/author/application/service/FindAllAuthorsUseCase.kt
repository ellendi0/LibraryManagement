package com.example.librarymanagement.author.application.service

import com.example.librarymanagement.author.application.port.`in`.FindAllAuthorsInPort
import com.example.librarymanagement.author.application.port.out.AuthorRepositoryOutPort
import com.example.librarymanagement.author.domain.Author
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllAuthorsUseCase(
    private val authorRepository: AuthorRepositoryOutPort
) : FindAllAuthorsInPort {
    override fun findAllAuthors(): Flux<Author> = authorRepository.findAll()
}
