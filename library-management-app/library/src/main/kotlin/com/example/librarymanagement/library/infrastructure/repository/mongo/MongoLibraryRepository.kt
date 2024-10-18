package com.example.librarymanagement.library.infrastructure.repository.mongo

import com.example.librarymanagement.library.application.port.out.LibraryRepositoryOutPort
import com.example.librarymanagement.library.domain.Library
import com.example.librarymanagement.library.infrastructure.convertor.LibraryMapper
import com.example.librarymanagement.library.infrastructure.entity.MongoLibrary
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoLibraryRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val libraryMapper: LibraryMapper
) : LibraryRepositoryOutPort {
    private fun Library.toEntity() = libraryMapper.toEntity(this)
    private fun MongoLibrary.toDomain() = libraryMapper.toDomain(this)

    override fun save(library: Library): Mono<Library> {
        val result: Mono<MongoLibrary> = reactiveMongoTemplate.save(library.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(libraryId: String): Mono<Library> {
        val result: Mono<MongoLibrary> = reactiveMongoTemplate.findById(ObjectId(libraryId), MongoLibrary::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAll(): Flux<Library> {
        val result: Flux<MongoLibrary> = reactiveMongoTemplate.findAll(MongoLibrary::class.java)
        return result.map { it.toDomain() }
    }

    override fun deleteById(libraryId: String): Mono<Unit> =
        reactiveMongoTemplate.findAndRemove(
            Query(Criteria.where("_id").`is`(ObjectId(libraryId))), MongoLibrary::class.java
        ).then(Mono.just(Unit))

    override fun existsById(libraryId: String): Mono<Boolean> =
        reactiveMongoTemplate.exists(
            Query(Criteria.where("_id").`is`(ObjectId(libraryId))),
            MongoLibrary::class.java
        )

    override fun existsLibraryWithAvailableBook(bookId: String, libraryId: String): Mono<Boolean> {
        val query = Query(
            Criteria
                .where("bookId").`is`(ObjectId(bookId))
                .and("libraryId").`is`(ObjectId(libraryId))
                .and("availability").`is`("AVAILABLE")
        )
        return reactiveMongoTemplate.exists(query, "presence_of_book")
    }
}
