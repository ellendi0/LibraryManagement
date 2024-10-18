package com.example.librarymanagement.publisher.application.port.`in`

import com.example.librarymanagement.publisher.domain.Publisher
import reactor.core.publisher.Mono

interface CreatePublisherInPort {
    fun createPublisher(publisher: Publisher): Mono<Publisher>
}
