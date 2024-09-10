package com.example.librarymanagement.repository.jpa

import com.example.librarymanagement.data.UserDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class JpaUserRepositoryTest {
    private val userRepositorySpring: UserRepositorySpring = mockk()
    private val jpaUserRepository = JpaUserRepository(userRepositorySpring)
    private val jpaUser = UserDataFactory.createJpaUser()

    private val jpaId = UserDataFactory.JPA_ID
    private val id = jpaId.toString()

    private val user = UserDataFactory.createUser()

    @Test
    fun `should find all users`() {
        //GIVEN
        val expected = listOf(user)

        every { userRepositorySpring.findAll() } returns listOf(jpaUser)

        //WHEN
        val actual = jpaUserRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should save user`() {
        //GIVEN
        val expected = user

        every { userRepositorySpring.save(any()) } returns jpaUser

        //WHEN
        val actual = jpaUserRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify (exactly = 1) { userRepositorySpring.save(any()) }
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = user

        every { userRepositorySpring.findById(jpaId) } returns Optional.of(jpaUser)

        //WHEN
        val actual = jpaUserRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete user by id`() {
        //GIVEN
        every { userRepositorySpring.deleteById(jpaId) } returns Unit

        //WHEN
        jpaUserRepository.deleteById(id)

        //THEN
        verify { userRepositorySpring.deleteById(jpaId) }
    }

    @Test
    fun `should return true if exists by email`() {
        //GIVEN
        val email = "test@test.com"

        every { userRepositorySpring.existsByEmail(email) } returns true

        //WHEN
        jpaUserRepository.existsByEmail(email)

        //THEN
        verify { userRepositorySpring.existsByEmail(email) }
    }

    @Test
    fun `should return true if exists by phone number`() {
        //GIVEN
        val phoneNumber = "0111111111"

        every { userRepositorySpring.existsByPhoneNumber(phoneNumber) } returns true

        //WHEN
        jpaUserRepository.existsByPhoneNumber(phoneNumber)

        //THEN
        verify { userRepositorySpring.existsByPhoneNumber(phoneNumber) }
    }

    @Test
    fun `should find user by email where phone number is null`() {
        //GIVEN
        val expected = user
        val email = "test@test.com"

        every { userRepositorySpring.findByEmailOrPhoneNumber(any(), null) } returns jpaUser

        //WHEN
        val actual = jpaUserRepository.findByEmailOrPhoneNumber(email, null)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should find user by phone number where user is null`() {
        //GIVEN
        val expected = user
        val phoneNumber = "0111111111"

        every { userRepositorySpring.findByEmailOrPhoneNumber(null, phoneNumber) } returns jpaUser

        //WHEN
        val actual = jpaUserRepository.findByEmailOrPhoneNumber(null, phoneNumber)

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
