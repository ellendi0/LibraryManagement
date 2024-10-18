package com.example.librarymanagement.publisher.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.publisher.application.port.`in`.GetPublisherByIdInPort
import com.example.librarymanagement.publisher.application.port.out.PublisherRepositoryOutPort
import com.example.librarymanagement.publisher.domain.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GetPublisherByIdUseCase(
    @Qualifier("redisPublisherRepo") private val publisherRepository: PublisherRepositoryOutPort
) : GetPublisherByIdInPort {
    override fun getPublisherById(id: String): Mono<Publisher> {
        return publisherRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Publisher")))
    }
}
