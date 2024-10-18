package com.example.librarymanagement.user.application.port.out

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.domain.User

interface UserRepositoryOutPort {
    fun save(user: User): Mono<User>
    fun findById(userId: String): Mono<User>
    fun findAll(): Flux<User>
    fun deleteById(userId: String): Mono<Unit>
    fun existsById(userId: String): Mono<Boolean>
    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByPhoneNumber(phoneNumber: String): Mono<Boolean>
}
