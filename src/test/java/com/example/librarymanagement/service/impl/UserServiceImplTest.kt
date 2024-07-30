package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.repository.BookPresenceRepository
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.JournalService
import com.example.librarymanagement.service.ReservationService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class UserServiceImplTest {
    private val userRepository: UserRepository = mockk()
    private val reservationService: ReservationService = mockk()
    private val journalService: JournalService = mockk()
    private val bookPresenceService: BookPresenceService = mockk()
    private val bookPresenceRepository: BookPresenceRepository = mockk()
    private val userService = UserServiceImpl(
        userRepository,
        journalService,
        reservationService,
        bookPresenceRepository,
        bookPresenceService
    )

    private val testDataRel = TestDataFactory.createTestDataRelForServices()
    private val user = testDataRel.user
    private val journal = testDataRel.journal
    private val reservation = testDataRel.reservation
    private val bookPresence = testDataRel.bookPresence
    private val library = testDataRel.library
    private val book = testDataRel.book

    @Test
    fun shouldCreateUser() {
        every { userRepository.existsByEmail(user.email) } returns false
        every { userRepository.existsByPhoneNumber(user.phoneNumber) } returns false
        every { userRepository.save(user) } returns user

        Assertions.assertEquals(user, userService.createUser(user))
        verify(exactly = 1) { userRepository.save(user) }
    }

    @Test
    fun shouldUpdateUser() {
        val updatedUser = user.copy(firstName = "Updated")
        every { userRepository.existsByEmail(updatedUser.email) } returns false
        every { userRepository.existsByPhoneNumber(updatedUser.phoneNumber) } returns false
        every { userRepository.findById(1) } returns Optional.of(user)
        every { userRepository.save(updatedUser) } returns updatedUser

        Assertions.assertEquals(updatedUser, userService.updateUser(1, updatedUser))
        verify(exactly = 1) { userRepository.findById(1) }
        verify(exactly = 1) { userRepository.save(updatedUser) }
    }

    @Test
    fun shouldGetUserById() {
        every { userRepository.findById(1) } returns Optional.of(user)

        Assertions.assertEquals(user, userService.getUserById(1))
        verify(exactly = 1) { userRepository.findById(1) }
    }

    @Test
    fun shouldGetUserByPhoneNumberOrEmail() {
        every { userRepository.findByEmailOrPhoneNumber("email", "phoneNumber") } returns user

        Assertions.assertEquals(user, userService.getUserByPhoneNumberOrEmail("email", "phoneNumber"))
        verify(exactly = 1) { userRepository.findByEmailOrPhoneNumber("email", "phoneNumber") }
    }

    @Test
    fun shouldFindAll(){
        every { userRepository.findAll() } returns(listOf(user))

        Assertions.assertEquals(listOf(user), userService.findAll())
        verify(exactly = 1){ userRepository.findAll() }
    }

    @Test
    fun shouldFindJournalByUserId(){
        every { journalService.getJournalByUserId(1) } returns listOf(journal)

        Assertions.assertEquals(listOf(journal), userService.findJournalsByUser(1))
        verify(exactly = 1){ journalService.getJournalByUserId(1) }
    }

    @Test
    fun shouldFindReservationsByUserId() {
        every { reservationService.getReservationsByUserId(1) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), userService.findReservationsByUser(1))
        verify(exactly = 1) { reservationService.getReservationsByUserId(1) }
    }

    @Test
    fun shouldBorrowBookFromLibrary() {
        every { userRepository.findById(1) } returns Optional.of(user)
        every { bookPresenceService.addUserToBook(user, library.id!!, bookPresence.id!!) } returns bookPresence

        Assertions.assertEquals(listOf(journal), userService.borrowBookFromLibrary(user.id!!, library.id!!, bookPresence.id!!))
        verify(exactly = 1) { bookPresenceService.addUserToBook(user, library.id!!, bookPresence.id!!) }
    }

    @Test
    fun shouldReturnBookToLibrary() {
        every { userRepository.findById(1) } returns Optional.of(user)
        every { bookPresenceService.removeUserFromBook(user, library.id!!, bookPresence.id!!) } returns bookPresence

        Assertions.assertEquals(listOf(journal), userService.returnBookToLibrary(user.id!!, library.id!!, bookPresence.id!!))
        verify(exactly = 1) { bookPresenceService.removeUserFromBook(user, library.id!!, bookPresence.id!!) }
    }

    @Test
    fun shouldReserveBookInLibrary() {
        every { userRepository.findById(1) } returns Optional.of(user)
        every { reservationService.reserveBook(user, library.id!!, bookPresence.id!!) } returns listOf(reservation)

        Assertions.assertEquals(listOf(reservation), userService.reserveBookInLibrary(user.id!!, library.id!!, bookPresence.id!!))
        verify(exactly = 1) { reservationService.reserveBook(user, library.id!!, bookPresence.id!!) }
    }

    @Test
    fun shouldCancelReservation() {
        every { userRepository.findById(1) } returns Optional.of(user)
        every { reservationService.removeReservation(user, book.id!!) } returns Unit

        userService.cancelReservationInLibrary(user.id!!, book.id!!)
        verify(exactly = 1) { reservationService.removeReservation(user, book.id!!) }
    }
}