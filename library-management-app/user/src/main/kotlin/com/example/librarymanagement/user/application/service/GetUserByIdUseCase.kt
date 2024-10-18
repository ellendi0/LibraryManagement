package com.example.librarymanagement.user.application.service

import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.GetUserByIdInPort
import com.example.librarymanagement.user.application.port.out.UserRepositoryOutPort
import com.example.librarymanagement.user.domain.User

@Service
class GetUserByIdUseCase(private val userRepository: UserRepositoryOutPort) : GetUserByIdInPort {
    override fun getUserById(id: String): Mono<User> =
        userRepository.findById(id).switchIfEmpty(Mono.error(EntityNotFoundException("User")))
}
