package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.jpa.JpaUser
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.repository.jpa.mapper.JpaUserMapper
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaUserRepository(
        private val userRepository: UserRepositorySpring,
        private val bookPresenceRepository: JpaBookPresenceRepository,
        private val journalRepository: JpaJournalRepository,
        private val reservationRepository: JpaReservationRepository
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

    @Transactional
    override fun deleteUser(userId: String) {
        userRepository.findByIdOrNull(userId.toLong())?.let { user ->

            bookPresenceRepository.findAllByUserId(userId)
                .takeIf { it.isNotEmpty() }
                ?.forEach { bookPresence ->
                    bookPresenceRepository.removeUserFromBook(
                        JpaUserMapper.toDomain(user),
                        bookPresence.library.id!!,
                        bookPresence.book.id!!
                    )
                }

            user.journals.forEach { journal ->
                journalRepository.deleteById(journal.id.toString())
            }

            user.reservations.forEach { reservation ->
                reservationRepository.deleteById(reservation.id.toString())
            }

            userRepository.delete(user)
        }
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
