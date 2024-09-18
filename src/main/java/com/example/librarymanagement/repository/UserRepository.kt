package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository {
    fun save(user: User): Mono<User>
    fun findById(userId: String): Mono<User>
    fun findAll(): Flux<User>
    fun deleteById(userId: String): Mono<Unit>
    fun existsById(userId: String): Mono<Boolean>
    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByPhoneNumber(phoneNumber: String): Mono<Boolean>
    fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): Mono<User>
}
