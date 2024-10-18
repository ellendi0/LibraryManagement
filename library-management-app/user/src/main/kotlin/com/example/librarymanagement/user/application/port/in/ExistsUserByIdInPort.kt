package com.example.librarymanagement.user.application.port.`in`

import reactor.core.publisher.Mono

interface ExistsUserByIdInPort {
    fun existsUserById(userId: String): Mono<Boolean>
}
