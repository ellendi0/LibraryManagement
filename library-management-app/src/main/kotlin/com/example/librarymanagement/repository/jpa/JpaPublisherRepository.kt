package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.jpa.JpaPublisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaPublisherMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
@Profile("jpa")
class JpaPublisherRepository(
    private val publisherRepository: PublisherRepositorySpring
) : PublisherRepository {
    fun Publisher.toEntity() = JpaPublisherMapper.toEntity(this)
    fun JpaPublisher.toDomain() = JpaPublisherMapper.toDomain(this)

    override fun save(publisher: Publisher): Mono<Publisher> =
        Mono.fromCallable { publisherRepository.save(publisher.toEntity()) }.map { it.toDomain() }
            .subscribeOn(Schedulers.boundedElastic())

    override fun findById(publisherId: String): Mono<Publisher> =
        Mono.fromCallable { publisherRepository.findByIdOrNull(publisherId.toLong())?.toDomain() }

    override fun findAll(): Flux<Publisher> = Flux.fromIterable(publisherRepository.findAll().map { it.toDomain() })
}

@Repository
interface PublisherRepositorySpring : JpaRepository<JpaPublisher, Long>
