package com.example.librarymanagement.service.impl

import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.repository.UserRepository
import com.example.librarymanagement.service.JournalService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserServiceImplTest {
    private val userRepository: UserRepository = mockk()
    private val journalService: JournalService = mockk()
    private val userService = UserServiceImpl(
        userRepository,
        journalService,
    )
    private val id = "1"

    private val user = UserDataFactory.createUser(id)
    private val journal = JournalDataFactory.createJournal(id)

    @Test
    fun shouldCreateUser() {
        //GIVEN
        val expected = user

        every { userRepository.existsByEmail(user.email) } returns false
        every { userRepository.existsByPhoneNumber(user.phoneNumber) } returns false
        every { userRepository.save(user) } returns user

        //WHEN
        val actual = userService.createUser(user)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { userRepository.save(user) }
    }

    @Test
    fun shouldUpdateUser() {
        //GIVEN
        val expected = user.copy(firstName = "Updated")

        every { userRepository.existsByEmail(expected.email) } returns false
        every { userRepository.existsByPhoneNumber(expected.phoneNumber) } returns false
        every { userRepository.findById(id) } returns user
        every { userRepository.save(expected) } returns expected

        //WHEN
        val actual = userService.updateUser(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { userRepository.findById(id) }
        verify(exactly = 1) { userRepository.save(expected) }
    }

    @Test
    fun shouldGetUserById() {
        //GIVEN
        val expected = user

        every { userRepository.findById(id) } returns user

        //WHEN
        val actual = userService.getUserById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { userRepository.findById(id) }
    }

    @Test
    fun shouldGetUserByPhoneNumberOrEmail() {
        //GIVEN
        val expected = user

        every { userRepository.findByEmailOrPhoneNumber("email", "phoneNumber") } returns user

        //WHEN
        val actual = userService.getUserByPhoneNumberOrEmail("email", "phoneNumber")

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { userRepository.findByEmailOrPhoneNumber("email", "phoneNumber") }
    }

    @Test
    fun shouldFindAll() {
        //GIVEN
        val expected = listOf(user)

        every { userRepository.findAll() } returns (listOf(user))

        //WHEN
        val actual = userService.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { userRepository.findAll() }
    }

    @Test
    fun shouldFindJournalByUserId() {
        //GIVEN
        val expected = listOf(journal)

        every { journalService.getJournalByUserId(id) } returns listOf(journal)

        //WHEN
        val actual = journalService.getJournalByUserId(id)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { journalService.getJournalByUserId(id) }
    }
}
