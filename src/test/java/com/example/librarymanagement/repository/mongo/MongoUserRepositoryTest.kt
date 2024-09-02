package com.example.librarymanagement.repository.mongo

import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.model.mongo.MongoUser

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.MongoTemplate

class MongoUserRepositoryTest {
    private val mongoTemplate: MongoTemplate = mockk()
    private val mongoUserRepository = MongoUserRepository(mongoTemplate)

    private val mongoId = UserDataFactory.MONGO_ID
    private val id = mongoId.toString()

    private val user = UserDataFactory.createUser(mongoId)
    private val mongoUser = UserDataFactory.createMongoUser()

    @Test
    fun `should find all users`() {
        //GIVEN
        val expected = listOf(user)

        every { mongoTemplate.findAll(MongoUser::class.java) } returns listOf(mongoUser)

        //WHEN
        val actual = mongoUserRepository.findAll()

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should save user`() {
        //GIVEN
        val expected = user

        every { mongoTemplate.save(mongoUser) } returns mongoUser

        //WHEN
        val actual = mongoUserRepository.save(expected)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.save(mongoUser) }
    }

    @Test
    fun `should find by id`() {
        //GIVEN
        val expected = user

        every { mongoTemplate.findById(mongoId, MongoUser::class.java) } returns mongoUser

        //WHEN
        val actual = mongoUserRepository.findById(id)

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should delete user by id`() {
        //GIVEN
        every { mongoTemplate.findAndRemove(any(), MongoUser::class.java) } returns mongoUser

        //WHEN
        mongoUserRepository.deleteById(id)

        //THEN
        verify(exactly = 1) { mongoTemplate.findAndRemove(any(), MongoUser::class.java) }
    }

    @Test
    fun `should return true if exists by email`() {
        //GIVEN
        val email = "test@test.com"
        val expected = true

        every { mongoTemplate.exists(any(), MongoUser::class.java) } returns true

        //WHEN
        val actual = mongoUserRepository.existsByEmail(email)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.exists(
            match{
                it.queryObject[MongoUser::email.name] == email
            }, MongoUser::class.java) }
    }

    @Test
    fun `should return true if exists by phone number`() {
        //GIVEN
        val phoneNumber = "0111111111"
        val expected = true

        every { mongoTemplate.exists(any(), MongoUser::class.java) } returns true

        //WHEN
        val actual = mongoUserRepository.existsByPhoneNumber(phoneNumber)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) { mongoTemplate.exists(
            match{
                it.queryObject[MongoUser::phoneNumber.name] == phoneNumber
            }, MongoUser::class.java) }
    }

    @Test
    fun `should find user by email where phone number is null`() {
        //GIVEN
        val expected = user
        val email = "test@test.com"

        every { mongoTemplate.findOne(any(), MongoUser::class.java) } returns mongoUser

        //WHEN
        val actual = mongoUserRepository.findByEmailOrPhoneNumber(email, null)

        //THEN
        Assertions.assertEquals(expected, actual)
        verify(exactly = 1) {
            mongoTemplate.findOne(
                match {
                    it.queryObject[MongoUser::email.name] == email &&
                            it.queryObject[MongoUser::phoneNumber.name] == null &&
                            it.queryObject["\$or"] != null
                },
                MongoUser::class.java
            )
        }
    }
}
