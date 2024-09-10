package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.jpa.JpaUser
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
@Profile("jpa")
class JpaUserRepository(
        private val userRepository: UserRepositorySpring
) : UserRepository{
    private fun User.toEntity() = JpaUserMapper.toEntity(this)
    private fun JpaUser.toDomain() = JpaUserMapper.toDomain(this)

    override fun save(user: User): User {
        return userRepository.save(user.toEntity()).toDomain()
    }

    override fun findById(userId: String): User? {
        return userRepository.findByIdOrNull(userId.toLong())?.toDomain()
    }

    override fun findAll(): List<User> {
        return userRepository.findAll().map { it.toDomain() }
    }

    override fun deleteById(userId: String) {
        return userRepository.deleteById(userId.toLong())
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun existsByPhoneNumber(phoneNumber: String): Boolean {
        return userRepository.existsByPhoneNumber(phoneNumber)
    }

    override fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): User? {
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber)?.toDomain()
    }
}

@Repository
interface UserRepositorySpring : JpaRepository<JpaUser, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByEmailOrPhoneNumber(email: String?, phoneNumber: String?): JpaUser?
}
