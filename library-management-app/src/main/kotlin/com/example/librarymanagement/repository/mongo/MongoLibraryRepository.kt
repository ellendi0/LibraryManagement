package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.mongo.MongoLibrary
import com.example.librarymanagement.repository.LibraryRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoLibraryMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("mongo")
class MongoLibraryRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : LibraryRepository {
    private fun Library.toEntity() = MongoLibraryMapper.toEntity(this)
    private fun MongoLibrary.toDomain() = MongoLibraryMapper.toDomain(this)

    override fun save(library: Library): Mono<Library> =
        reactiveMongoTemplate.save(library.toEntity()).map { it.toDomain() }

    override fun findById(libraryId: String): Mono<Library> =
        reactiveMongoTemplate.findById(ObjectId(libraryId), MongoLibrary::class.java).map { it.toDomain() }

    override fun findAll(): Flux<Library> =
        reactiveMongoTemplate.findAll(MongoLibrary::class.java).map { it.toDomain() }

    override fun deleteById(libraryId: String): Mono<Unit> =
        reactiveMongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(ObjectId(libraryId))), MongoLibrary::class.java
        ).then(Mono.just(Unit))

    override fun existsById(libraryId: String): Mono<Boolean> =
        reactiveMongoTemplate.exists(
            Query(Criteria.where("_id").`is`(ObjectId(libraryId))),
            MongoLibrary::class.java
        )
}
