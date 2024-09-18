package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PublisherService {
    fun createPublisher(publisher: Publisher): Mono<Publisher>
    fun updatePublisher(updatedPublisher: Publisher): Mono<Publisher>
    fun getPublisherById(id: String): Mono<Publisher>
    fun getAllPublishers(): Flux<Publisher>
}
