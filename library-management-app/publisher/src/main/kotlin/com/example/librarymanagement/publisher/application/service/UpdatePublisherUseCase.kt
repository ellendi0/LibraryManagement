package com.example.librarymanagement.publisher.application.service

import com.example.librarymanagement.publisher.application.port.`in`.GetPublisherByIdInPort
import com.example.librarymanagement.publisher.application.port.`in`.UpdatePublisherInPort
import com.example.librarymanagement.publisher.application.port.out.PublisherRepositoryOutPort
import com.example.librarymanagement.publisher.domain.Publisher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdatePublisherUseCase(
    @Qualifier("redisPublisherRepo") private val publisherRepository: PublisherRepositoryOutPort,
    private val getPublisherByIdInPort: GetPublisherByIdInPort
) : UpdatePublisherInPort {
    override fun updatePublisher(updatedPublisher: Publisher): Mono<Publisher> {
        return getPublisherByIdInPort.getPublisherById(updatedPublisher.id!!)
            .map { it.copy(name = updatedPublisher.name) }
            .flatMap { publisherRepository.save(it) }
    }
}
