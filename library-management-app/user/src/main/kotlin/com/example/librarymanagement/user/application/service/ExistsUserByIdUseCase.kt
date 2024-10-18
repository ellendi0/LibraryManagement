package com.example.librarymanagement.user.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.ExistsUserByIdInPort
import com.example.librarymanagement.user.application.port.out.UserRepositoryOutPort

@Service
class ExistsUserByIdUseCase(private val userRepository: UserRepositoryOutPort) : ExistsUserByIdInPort {
    override fun existsUserById(userId: String): Mono<Boolean> =
        userRepository.existsById(userId)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("User")) else Mono.just(it) }
}
