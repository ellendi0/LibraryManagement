package com.example.librarymanagement.publisher.application.service

import com.example.librarymanagement.publisher.application.port.`in`.FindAllPublishersInPort
import com.example.librarymanagement.publisher.application.port.out.PublisherRepositoryOutPort
import com.example.librarymanagement.publisher.domain.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllPublishersUseCase(
    @Qualifier("redisPublisherRepo") private val publisherRepository: PublisherRepositoryOutPort
) : FindAllPublishersInPort {
    override fun findAllPublishers(): Flux<Publisher> = publisherRepository.findAll()
}
