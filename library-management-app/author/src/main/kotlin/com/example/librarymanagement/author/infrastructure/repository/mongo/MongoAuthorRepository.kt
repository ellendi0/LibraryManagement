package com.example.librarymanagement.author.infrastructure.repository.mongo

import com.example.librarymanagement.author.application.port.out.AuthorRepositoryOutPort
import com.example.librarymanagement.author.domain.Author
import com.example.librarymanagement.author.infrastructure.convertor.AuthorMapper
import com.example.librarymanagement.author.infrastructure.entity.MongoAuthor
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoAuthorRepository(
    private val authorMapper: AuthorMapper,
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : AuthorRepositoryOutPort {
    private fun Author.toEntity() = authorMapper.toEntity(this)
    private fun MongoAuthor.toDomain() = authorMapper.toDomain(this)

    override fun save(author: Author): Mono<Author> {
        val result: Mono<MongoAuthor> = reactiveMongoTemplate.save(author.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(authorId: String): Mono<Author> {
        val result: Mono<MongoAuthor> = reactiveMongoTemplate.findById(ObjectId(authorId), MongoAuthor::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAll(): Flux<Author> {
        val result: Flux<MongoAuthor> = reactiveMongoTemplate.findAll(MongoAuthor::class.java)
        return result.map { it.toDomain() }
    }
}
