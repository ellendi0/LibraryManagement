package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.mongo.MongoPublisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.repository.mongo.mapper.MongoPublisherMapper
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("mongo")
class MongoPublisherRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : PublisherRepository {
    fun Publisher.toEntity() = MongoPublisherMapper.toEntity(this)
    fun MongoPublisher.toDomain() = MongoPublisherMapper.toDomain(this)

    override fun save(publisher: Publisher): Mono<Publisher> =
        reactiveMongoTemplate.save(publisher.toEntity()).map { it.toDomain() }

    override fun findById(publisherId: String): Mono<Publisher> =
        reactiveMongoTemplate.findById(ObjectId(publisherId), MongoPublisher::class.java).map { it.toDomain() }

    override fun findAll(): Flux<Publisher> =
        reactiveMongoTemplate.findAll(MongoPublisher::class.java).map { it.toDomain() }
}
