package com.example.librarymanagement.publisher.application.port.`in`

import com.example.librarymanagement.publisher.domain.Publisher
import reactor.core.publisher.Mono

interface GetPublisherByIdInPort {
    fun getPublisherById(id: String): Mono<Publisher>
}
