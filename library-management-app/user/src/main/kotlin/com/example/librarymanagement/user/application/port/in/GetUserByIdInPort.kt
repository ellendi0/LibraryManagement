package com.example.librarymanagement.user.application.port.`in`

import reactor.core.publisher.Mono
import com.example.librarymanagement.user.domain.User

interface GetUserByIdInPort {
    fun getUserById(id: String): Mono<User>
}
