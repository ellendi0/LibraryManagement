package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.mongo.MongoBook
import com.example.librarymanagement.repository.BookRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoBookMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoBookRepository(
    private val mongoTemplate: MongoTemplate
) : BookRepository {
    private fun Book.toEntity() = MongoBookMapper.toEntity(this)
    private fun MongoBook.toDomain() = MongoBookMapper.toDomain(this)

    override fun save(book: Book): Book {
        return mongoTemplate.save(book.toEntity()).toDomain()
    }

    override fun findById(bookId: String): Book? {
        return mongoTemplate.findById(bookId, MongoBook::class.java)?.toDomain()
    }

    override fun findAll(): List<Book> {
        return mongoTemplate.findAll(MongoBook::class.java).map { it.toDomain() }
    }

    override fun deleteById(bookId: String) {
        mongoTemplate.findAndRemove(Query(Criteria.where("_id").`is`(ObjectId(bookId))), MongoBook::class.java)
    }

    override fun existsByIsbn(isbn: Long): Boolean {
        return mongoTemplate.exists(Query(Criteria.where(MongoBook::isbn.name).`is`(isbn)), MongoBook::class.java)
    }

    override fun findBookByTitleAndAuthorId(title: String, authorId: String): Book? {
        val query = Query(Criteria
            .where(MongoBook::title.name).`is`(title)
            .and(MongoBook::authorId.name).`is`(ObjectId(authorId)))
        return mongoTemplate.findOne(query, MongoBook::class.java)?.toDomain()
    }
}
