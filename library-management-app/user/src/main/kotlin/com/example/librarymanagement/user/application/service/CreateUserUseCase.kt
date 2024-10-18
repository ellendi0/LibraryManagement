package com.example.librarymanagement.user.application.service

import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.CreateUserInPort
import com.example.librarymanagement.user.application.port.out.UserRepositoryOutPort
import com.example.librarymanagement.user.domain.User

@Service
class CreateUserUseCase(private val userRepository: UserRepositoryOutPort) : CreateUserInPort {
    override fun createUser(user: User): Mono<User> {
        return Mono.zip(
            userRepository.existsByEmail(user.email),
            userRepository.existsByPhoneNumber(user.phoneNumber)
        )
            .flatMap {
                val emailExists = it.t1
                val phoneExists = it.t2

                when {
                    emailExists -> Mono.error(DuplicateKeyException("User", "email"))
                    phoneExists -> Mono.error(DuplicateKeyException("User", "phoneNumber"))
                    else -> userRepository.save(user)
                }
            }
    }}
