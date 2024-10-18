package com.example.librarymanagement.publisher.infrastructure.repository.mongo

import com.example.librarymanagement.publisher.application.port.out.PublisherRepositoryOutPort
import com.example.librarymanagement.publisher.domain.Publisher
import com.example.librarymanagement.publisher.infrastructure.convertor.PublisherMapper
import com.example.librarymanagement.publisher.infrastructure.entity.MongoPublisher
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository(value = "mongoPublisherRepo")
class MongoPublisherRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val publisherMapper: PublisherMapper
) : PublisherRepositoryOutPort {
    private fun Publisher.toEntity() = publisherMapper.toEntity(this)
    private fun MongoPublisher.toDomain() = publisherMapper.toDomain(this)

    override fun save(publisher: Publisher): Mono<Publisher> {
        val result: Mono<MongoPublisher> = reactiveMongoTemplate.save(publisher.toEntity())
        return result.map { it.toDomain() }
    }

    override fun findById(publisherId: String): Mono<Publisher> {
        val result: Mono<MongoPublisher> =
            reactiveMongoTemplate.findById(ObjectId(publisherId), MongoPublisher::class.java)
        return result.map { it.toDomain() }
    }

    override fun findAll(): Flux<Publisher> {
        val result: Flux<MongoPublisher> = reactiveMongoTemplate.findAll(MongoPublisher::class.java)
        return result.map { it.toDomain() }

    }
}
