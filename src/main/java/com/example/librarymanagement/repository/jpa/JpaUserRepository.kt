package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.jpa.JpaUser
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Repository
@Profile("jpa")
class JpaUserRepository(
    private val userRepository: UserRepositorySpring
) : UserRepository {
    private fun User.toEntity() = JpaUserMapper.toEntity(this)
    private fun JpaUser.toDomain() = JpaUserMapper.toDomain(this)

    override fun save(user: User): Mono<User> =
        Mono.fromCallable { userRepository.save(user.toEntity()).toDomain() }.subscribeOn(Schedulers.boundedElastic())

    override fun findById(userId: String): Mono<User> =
        Mono.fromCallable { userRepository.findByIdOrNull(userId.toLong())?.toDomain() }

    override fun findAll(): Flux<User> = Flux.fromIterable(userRepository.findAll().map { it.toDomain() })

    override fun deleteById(userId: String): Mono<Unit> =
        Mono.fromCallable { userRepository.deleteById(userId.toLong()) }
            .subscribeOn(Schedulers.boundedElastic())
            .then(Mono.just(Unit))

    override fun existsById(userId: String): Mono<Boolean> =
        Mono.fromCallable { userRepository.existsById(userId.toLong()) }.subscribeOn(Schedulers.boundedElastic())

    override fun existsByEmail(email: String): Mono<Boolean> =
        Mono.fromCallable { userRepository.existsByEmail(email) }.subscribeOn(Schedulers.boundedElastic())

    override fun existsByPhoneNumber(phoneNumber: String): Mono<Boolean> =
        Mono.fromCallable { userRepository.existsByPhoneNumber(phoneNumber) }.subscribeOn(Schedulers.boundedElastic())

    override fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): Mono<User> =
        Mono.fromCallable { userRepository.findByEmailOrPhoneNumber(email, phoneNumber) }
            .subscribeOn(Schedulers.boundedElastic())
            .mapNotNull { it?.toDomain() }
}

@Repository
interface UserRepositorySpring : JpaRepository<JpaUser, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): JpaUser?
}
