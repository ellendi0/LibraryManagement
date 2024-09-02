package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val journalService: JournalService,
) : UserService {

    override fun getUserByPhoneNumberOrEmail(email: String?, phoneNumber: String?): User {
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber) ?: throw EntityNotFoundException("User")
    }

    override fun getUserById(id: String): User {
        return userRepository.findById(id) ?: throw EntityNotFoundException("User")
    }

    override fun createUser(user: User): User {
        if (userRepository.existsByEmail(user.email)) throw DuplicateKeyException("User", "email")
        if (userRepository.existsByPhoneNumber(user.phoneNumber)) throw DuplicateKeyException("User", "phoneNumber")
        return userRepository.save(user)
    }

    override fun updateUser(updatedUser: User): User {
        if (userRepository.existsByEmail(updatedUser.email)) throw DuplicateKeyException("User", "email")
        if (userRepository.existsByPhoneNumber(updatedUser.phoneNumber)) throw DuplicateKeyException(
            "User",
            "phoneNumber"
        )
        val user = getUserById(updatedUser.id!!).copy(
            email = updatedUser.email,
            phoneNumber = updatedUser.phoneNumber,
            firstName = updatedUser.firstName,
            lastName = updatedUser.lastName
        )
        return userRepository.save(user)
    }

    override fun findAll(): List<User> = userRepository.findAll()

    override fun findJournalsByUser(userId: String): List<Journal> {
        return journalService.getJournalByUserId(userId)
    }
}
