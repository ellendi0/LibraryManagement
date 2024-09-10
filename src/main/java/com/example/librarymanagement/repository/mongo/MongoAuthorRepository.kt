package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.mongo.MongoAuthor
import com.example.librarymanagement.repository.AuthorRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoAuthorMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoAuthorRepository(
    private val mongoTemplate: MongoTemplate
): AuthorRepository{
    private fun Author.toEntity() = MongoAuthorMapper.toEntity(this)
    private fun MongoAuthor.toDomain() = MongoAuthorMapper.toDomain(this)

    override fun save(author: Author): Author {
        return mongoTemplate.save(author.toEntity()).toDomain()
    }

    override fun findById(authorId: String): Author? {
        return mongoTemplate.findById(ObjectId(authorId), MongoAuthor::class.java)?.toDomain()
    }

    override fun findAll(): List<Author> {
       return mongoTemplate.findAll(MongoAuthor::class.java).map { it.toDomain() }
    }
}
