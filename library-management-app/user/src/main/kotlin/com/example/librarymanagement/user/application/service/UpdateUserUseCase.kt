package com.example.librarymanagement.user.application.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.GetUserByIdInPort
import com.example.librarymanagement.user.application.port.`in`.UpdateUserInPort
import com.example.librarymanagement.user.application.port.out.UserRepositoryOutPort
import com.example.librarymanagement.user.domain.User

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepositoryOutPort,
    private val getUserByIdInPort: GetUserByIdInPort
) : UpdateUserInPort {
    override fun updateUser(updatedUser: User): Mono<User> {
        return getUserByIdInPort.getUserById(updatedUser.id!!)
            .map { it.copy(firstName = updatedUser.firstName, lastName = updatedUser.lastName) }
            .flatMap { userRepository.save(it) }
    }
}
