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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class UserServiceImplTest {
    private val userRepository: UserRepository = mockk()
    private val journalService: JournalService = mockk()
    private val userService = UserServiceImpl(
        userRepository,
        journalService,
    )
    private val id = UserDataFactory.ID

    private val user = UserDataFactory.createUser(id)
    private val journal = JournalDataFactory.createJournal(id)

    @Test
    fun `should create user`() {
        // GIVEN
        val expected = user

        every { userRepository.existsByEmail(user.email) } returns Mono.just(false)
        every { userRepository.existsByPhoneNumber(user.phoneNumber) } returns Mono.just(false)
        every { userRepository.save(user) } returns Mono.just(user)

        // WHEN
        val result = StepVerifier.create(userService.createUser(user))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { userRepository.save(any()) }
        }.verifyComplete()
    }

    @Test
    fun `should update user`() {
        // GIVEN
        val expected = user

        every { userRepository.existsByEmail(expected.email) } returns Mono.just(false)
        every { userRepository.existsByPhoneNumber(expected.phoneNumber) } returns Mono.just(false)
        every { userRepository.findById(id) } returns Mono.just(user)
        every { userRepository.save(user) } returns Mono.just(user)

        // WHEN
        val result = StepVerifier.create(userService.updateUser(user))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
            verify(exactly = 1) { userRepository.save(any()) }
        }.verifyComplete()
    }

    @Test
    fun `should get user by id`() {
        // GIVEN
        val expected = user

        every { userRepository.findById(id) } returns Mono.just(user)

        // WHEN
        val result = StepVerifier.create(userService.getUserById(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should get user by phone number or email`() {
        // GIVEN
        val expected = user

        every { userRepository.findByEmailOrPhoneNumber("email", "phoneNumber") }
            .returns(Mono.just(user))

        // WHEN
        val result = StepVerifier.create(userService
            .getUserByPhoneNumberOrEmail("email", "phoneNumber"))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should find all`() {
        // GIVEN
        val expected = user

        every { userRepository.findAll() } returns Flux.just(user)

        // WHEN
        val result = StepVerifier.create(userService.findAll())

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }

    @Test
    fun `should find journal by user id`() {
        // GIVEN
        val expected = journal

        every { journalService.getJournalByUserId(id) } returns Flux.just(journal)

        // WHEN
        val result = StepVerifier.create(userService.findJournalsByUser(id))

        // THEN
        result.assertNext { actual ->
            Assertions.assertEquals(expected, actual)
        }.verifyComplete()
    }
}
