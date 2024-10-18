package com.example.librarymanagement.publisher.infrastructure.repository.redis

import com.example.librarymanagement.publisher.application.port.out.PublisherRepositoryOutPort
import com.example.librarymanagement.publisher.domain.Publisher
import com.example.librarymanagement.publisher.infrastructure.repository.mongo.MongoPublisherRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

@Component("redisPublisherRepo")
class RedisPublisherRepository(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, Publisher>,
    private val mongoRepository: MongoPublisherRepository,
    @Value("\${spring.cache.redis.time-to-live}") private val timeToLive: String,
) : PublisherRepositoryOutPort {
    private val prefix: String = "publisher"

    override fun save(publisher: Publisher): Mono<Publisher> {
        return mongoRepository.save(publisher)
            .flatMap { saveEntityToCache(it) }
    }

    override fun findById(publisherId: String): Mono<Publisher> {
        return reactiveRedisTemplate.opsForValue()
            .get("$prefix$publisherId")
            .switchIfEmpty {mongoRepository.findById(publisherId).flatMap { saveEntityToCache(it) }}
    }

    override fun findAll(): Flux<Publisher> = mongoRepository.findAll()

    private fun saveEntityToCache(publisher: Publisher): Mono<Publisher> {
        val key = publisher.id
        return reactiveRedisTemplate.opsForValue()
            .set(
                prefix + key,
                publisher,
                Duration.ofMinutes(timeToLive.toLong())
            ).thenReturn(publisher)
    }
}
