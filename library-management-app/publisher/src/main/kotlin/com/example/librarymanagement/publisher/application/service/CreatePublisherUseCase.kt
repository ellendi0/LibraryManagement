package com.example.librarymanagement.publisher.application.service

import com.example.librarymanagement.publisher.application.port.`in`.CreatePublisherInPort
import com.example.librarymanagement.publisher.application.port.out.PublisherRepositoryOutPort
import com.example.librarymanagement.publisher.domain.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreatePublisherUseCase(
    @Qualifier("redisPublisherRepo") private val publisherRepository: PublisherRepositoryOutPort
) : CreatePublisherInPort {
    override fun createPublisher(publisher: Publisher): Mono<Publisher> = publisherRepository.save(publisher)
}
