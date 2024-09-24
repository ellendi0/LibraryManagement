package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.mongo.MongoBook
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoBookMapper
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
class MongoBookRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : BookRepository {
    private fun Book.toEntity() = MongoBookMapper.toEntity(this)
    private fun MongoBook.toDomain() = MongoBookMapper.toDomain(this)

    override fun save(book: Book): Mono<Book> =
        reactiveMongoTemplate.save(book.toEntity()).map { it.toDomain() }

    override fun findById(bookId: String): Mono<Book> =
        reactiveMongoTemplate.findById(bookId, MongoBook::class.java).map { it.toDomain() }

    override fun findAll(): Flux<Book> = reactiveMongoTemplate.findAll(MongoBook::class.java).map { it.toDomain() }

    override fun deleteById(bookId: String): Mono<Unit> =
        reactiveMongoTemplate
            .findAndRemove(Query(Criteria.where("_id").`is`(ObjectId(bookId))), MongoBook::class.java)
            .then(Mono.just(Unit))

    override fun existsById(bookId: String): Mono<Boolean> =
        reactiveMongoTemplate.exists(Query(Criteria.where("_id").`is`(bookId)), MongoBook::class.java)

    override fun existsByIsbn(isbn: Long): Mono<Boolean> =
        reactiveMongoTemplate.exists(Query(Criteria.where(MongoBook::isbn.name).`is`(isbn)), MongoBook::class.java)

    override fun findBookByTitleAndAuthorId(title: String, authorId: String): Flux<Book> {
        val query = Query(
            Criteria
                .where(MongoBook::title.name).`is`(title)
                .and(MongoBook::authorId.name).`is`(ObjectId(authorId))
        )
        return reactiveMongoTemplate.find(query, MongoBook::class.java).map { it.toDomain() }
    }
}
