package com.example.librarymanagement.book.infrastructure.repository.mongo

import com.example.librarymanagement.book.application.port.out.BookRepositoryOutPort
import com.example.librarymanagement.book.domain.Book
import com.example.librarymanagement.book.infrastructure.convertor.BookMapper
import com.example.librarymanagement.book.infrastructure.entity.MongoBook
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoBookRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val bookMapper: BookMapper
) : BookRepositoryOutPort {
    private fun Book.toEntity() = bookMapper.toEntity(this)
    private fun MongoBook.toDomain() = bookMapper.toDomain(this)

    override fun save(book: Book): Mono<Book> {
        val result: Mono<MongoBook> = reactiveMongoTemplate.save(book.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(bookId: String): Mono<Book> {
        val result: Mono<MongoBook> = reactiveMongoTemplate.findById(bookId, MongoBook::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAll(): Flux<Book> {
        val result: Flux<MongoBook> = reactiveMongoTemplate.findAll(MongoBook::class.java)
        return result.map { it.toDomain() }
    }

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
        val result: Flux<MongoBook> = reactiveMongoTemplate.find(query, MongoBook::class.java)
        return result.map { it.toDomain() }
    }
}
