package com.example.librarymanagement.publisher.application.port.out

import com.example.librarymanagement.publisher.domain.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PublisherRepositoryOutPort {
    fun save(publisher: Publisher): Mono<Publisher>
    fun findById(publisherId: String): Mono<Publisher>
    fun findAll(): Flux<Publisher>
}
