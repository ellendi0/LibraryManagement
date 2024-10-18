package com.example.librarymanagement.user.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import com.example.librarymanagement.user.application.port.`in`.FindAllUsersInPort
import com.example.librarymanagement.user.application.port.out.UserRepositoryOutPort
import com.example.librarymanagement.user.domain.User

@Service
class FindAllUsersInPortUseCase(private val userRepository: UserRepositoryOutPort) : FindAllUsersInPort {
    override fun findAllUsers(): Flux<User> = userRepository.findAll()
}
