package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.domain.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {
    fun getUserByPhoneNumberOrEmail(email: String?, phoneNumber: String?): Mono<User>
    fun getUserById(id: String): Mono<User>
    fun createUser(user: User): Mono<User>
    fun updateUser(updatedUser: User): Mono<User>
    fun findAll(): Flux<User>
    fun findJournalsByUser(userId: String): Flux<Journal>
    fun existsUserById(userId: String): Mono<Boolean>
}
