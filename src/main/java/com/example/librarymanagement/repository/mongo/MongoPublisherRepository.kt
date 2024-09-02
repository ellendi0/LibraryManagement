package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.mongo.MongoPublisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoPublisherMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Repository

@Repository
@Profile("mongo")
class MongoPublisherRepository(
    private val mongoTemplate: MongoTemplate
) : PublisherRepository {
    fun Publisher.toEntity() = MongoPublisherMapper.toEntity(this)
    fun MongoPublisher.toDomain() = MongoPublisherMapper.toDomain(this)

    override fun save(publisher: Publisher): Publisher {
        return mongoTemplate.save(publisher.toEntity()).toDomain()
    }

    override fun findById(publisherId: String): Publisher? {
        return mongoTemplate.findById(ObjectId(publisherId), MongoPublisher::class.java)?.toDomain()
    }

    override fun findAll(): List<Publisher> {
        return mongoTemplate.findAll(MongoPublisher::class.java).map { it.toDomain() }
    }
}
