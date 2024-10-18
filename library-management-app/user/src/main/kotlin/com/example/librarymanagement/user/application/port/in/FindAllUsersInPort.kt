package com.example.librarymanagement.user.application.port.`in`

import reactor.core.publisher.Flux
import com.example.librarymanagement.user.domain.User

interface FindAllUsersInPort {
    fun findAllUsers(): Flux<User>
}
