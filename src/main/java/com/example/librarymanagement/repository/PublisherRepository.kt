package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PublisherRepository{
    fun save(publisher: Publisher): Mono<Publisher>
    fun findById(publisherId: String): Mono<Publisher>
    fun findAll(): Flux<Publisher>
}
