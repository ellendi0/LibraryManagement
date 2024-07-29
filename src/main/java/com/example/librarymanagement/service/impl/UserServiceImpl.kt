package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.ReservationService
import com.example.librarymanagement.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val journalService: JournalService,
    private val reservationService: ReservationService,
    private val bookPresenceRepository: BookPresenceRepository,
    private val bookPresenceService: BookPresenceService
) : UserService {

    override fun getUserByPhoneNumberOrEmail(email: String?, phoneNumber: String?): User {
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber) ?: throw EntityNotFoundException("User")
    }

    override fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow { throw EntityNotFoundException("User") }
    }
    override fun createUser(user: User): User {
        if (userRepository.existsByEmail(user.email)) throw DuplicateKeyException("User", "email")
        if (userRepository.existsByPhoneNumber(user.phoneNumber)) throw DuplicateKeyException("User", "phoneNumber")
        return userRepository.save(user)
    }

    override fun updateUser(id: Long, updatedUser: User): User {
        if (userRepository.existsByEmail(updatedUser.email)) throw DuplicateKeyException("User", "email")
        if (userRepository.existsByPhoneNumber(updatedUser.phoneNumber)) throw DuplicateKeyException("User", "phoneNumber")
        val user = getUserById(id).apply {
            this.email = updatedUser.email
            this.phoneNumber = updatedUser.phoneNumber
            this.firstName = updatedUser.firstName
            this.lastName = updatedUser.lastName
        }
        return userRepository.save(user)
    }

    override fun findAll(): List<User> = userRepository.findAll()

    override fun findReservationsByUser(userId: Long): List<Reservation> {
        return reservationService.getReservationsByUserId(userId)
    }

    override fun findJournalsByUser(userId: Long): List<Journal> {
        return journalService.getJournalByUserId(userId)
    }

    override fun borrowBookFromLibrary(userId: Long, libraryId: Long, bookId: Long): List<Journal> {
        return bookPresenceService.addUserToBook(getUserById(userId), libraryId, bookId).journals
    }

    override fun reserveBookInLibrary(userId: Long, libraryId: Long, bookId: Long): List<Reservation> {
        return reservationService.reserveBook(getUserById(userId), libraryId, bookId)
    }

    override fun cancelReservationInLibrary(userId: Long, bookId: Long) {
        reservationService.removeReservation(getUserById(userId), bookId)
    }

    override fun returnBookToLibrary(userId: Long, libraryId: Long, bookId: Long): List<Journal> {
        return bookPresenceService.removeUserFromBook(getUserById(userId), libraryId, bookId).journals
    }

    @Transactional
    override fun deleteUser(id: Long) {
        userRepository.findById(id).ifPresent { user ->
            bookPresenceRepository.findAllByUserId(id)
                .takeIf { it.isNotEmpty() }
                ?.forEach { bookPresence ->
                    bookPresenceService.removeUserFromBook(user, bookPresence.library.id!!, bookPresence.book.id!!)
                }
            user.journals.forEach { journal -> journalService.deleteJournal(journal.id!!) }
            user.reservations.forEach { reservation -> reservationService.deleteReservationById(reservation.id!!) }

            userRepository.delete(user)
        }
    }
}