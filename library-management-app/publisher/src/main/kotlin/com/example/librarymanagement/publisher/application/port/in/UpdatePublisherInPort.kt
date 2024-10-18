package com.example.librarymanagement.publisher.application.port.`in`

import com.example.librarymanagement.publisher.domain.Publisher
import reactor.core.publisher.Mono

interface UpdatePublisherInPort {
    fun updatePublisher(updatedPublisher: Publisher): Mono<Publisher>
}
