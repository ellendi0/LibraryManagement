package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.jpa.JpaAuthor
import com.example.librarymanagement.repository.AuthorRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaAuthorMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
@Profile("jpa")
class JpaAuthorRepository(
    private val authorRepository: AuthorRepositorySpring,
) : AuthorRepository {
    private fun Author.toEntity() = JpaAuthorMapper.toEntity(this)
    private fun JpaAuthor.toDomain() = JpaAuthorMapper.toDomain(this)

    override fun save(author: Author): Mono<Author> =
        Mono.fromCallable { authorRepository.save(author.toEntity()) }
            .map { it.toDomain() }.subscribeOn(Schedulers.boundedElastic())

    override fun findById(authorId: String): Mono<Author> =
        Mono.fromCallable { authorRepository.findByIdOrNull(authorId.toLong())?.toDomain() }

    override fun findAll(): Flux<Author> = Flux.fromIterable(authorRepository.findAll().map { it.toDomain() })
}

@Repository
interface AuthorRepositorySpring : JpaRepository<JpaAuthor, Long>
