package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.repository.PublisherRepository
import com.example.librarymanagement.service.PublisherService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PublisherServiceImpl(private val publisherRepository: PublisherRepository) : PublisherService {

    override fun createPublisher(publisher: Publisher): Mono<Publisher> = publisherRepository.save(publisher)

    override fun updatePublisher(updatedPublisher: Publisher): Mono<Publisher> {
        return getPublisherById(updatedPublisher.id!!)
            .map { it.copy( name = updatedPublisher.name ) }
            .flatMap { publisherRepository.save(it) }
    }

    override fun getPublisherById(id: String): Mono<Publisher> {
        return publisherRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Publisher")))
    }

    override fun getAllPublishers(): Flux<Publisher> = publisherRepository.findAll()
}
