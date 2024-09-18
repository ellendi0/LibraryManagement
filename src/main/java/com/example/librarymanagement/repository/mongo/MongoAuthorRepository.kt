package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.mongo.MongoAuthor
import com.example.librarymanagement.repository.AuthorRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoAuthorMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("mongo")
class MongoAuthorRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : AuthorRepository {
    private fun Author.toEntity() = MongoAuthorMapper.toEntity(this)
    private fun MongoAuthor.toDomain() = MongoAuthorMapper.toDomain(this)

    override fun save(author: Author): Mono<Author> =
        reactiveMongoTemplate.save(author.toEntity()).map { it.toDomain() }

    override fun findById(authorId: String): Mono<Author> =
        reactiveMongoTemplate.findById(ObjectId(authorId), MongoAuthor::class.java).map { it.toDomain() }

    override fun findAll(): Flux<Author> =
        reactiveMongoTemplate.findAll(MongoAuthor::class.java).map { it.toDomain() }
}
