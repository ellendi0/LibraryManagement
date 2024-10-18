package com.example.librarymanagement.publisher.application.port.`in`

import com.example.librarymanagement.publisher.domain.Publisher
import reactor.core.publisher.Flux

interface FindAllPublishersInPort {
    fun findAllPublishers(): Flux<Publisher>
}
