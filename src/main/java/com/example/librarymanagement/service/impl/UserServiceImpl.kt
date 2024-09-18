package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.UserService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val journalService: JournalService,
) : UserService {

    override fun getUserByPhoneNumberOrEmail(email: String?, phoneNumber: String?): Mono<User> {
        return userRepository
            .findByEmailOrPhoneNumber(email, phoneNumber)
            .switchIfEmpty(Mono.error(EntityNotFoundException("User")))
    }

    override fun getUserById(id: String): Mono<User> =
        userRepository.findById(id).switchIfEmpty(Mono.error(EntityNotFoundException("User")))

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
    }

    override fun updateUser(updatedUser: User): Mono<User> {
        return getUserById(updatedUser.id!!)
            .map { it.copy(firstName = updatedUser.firstName, lastName = updatedUser.lastName) }
            .flatMap { userRepository.save(it) }
    }

    override fun findAll(): Flux<User> = userRepository.findAll()

    override fun findJournalsByUser(userId: String): Flux<Journal> = journalService.getJournalByUserId(userId)

    override fun existsUserById(userId: String): Mono<Boolean> =
        userRepository.existsById(userId)
            .flatMap { if (it == false) Mono.error(EntityNotFoundException("User")) else Mono.just(it) }
}
