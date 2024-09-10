package com.example.librarymanagement.data

import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.dto.UserResponseDto
import com.example.librarymanagement.dto.mapper.UserMapper
import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.jpa.JpaUser
import com.example.librarymanagement.model.mongo.MongoUser
import org.bson.types.ObjectId

object UserDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val FIRST_NAME = "Test"
    private const val LAST_NAME = "Test"
    private const val EMAIL = "test@example.com"
    private const val PHONE_NUMBER = "1234567890"
    private const val PASSWORD = "Password1"


    fun createUser(id: String = ID): User {
        return User(
            id = id,
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            phoneNumber = PHONE_NUMBER,
            password = PASSWORD
        )
    }

    fun createUserRequestDto(id: String = ID): UserRequestDto {
        return UserRequestDto(
            id = id,
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            phoneNumber = PHONE_NUMBER,
            password = PASSWORD
        )
    }

    fun createUserResponseDto(): UserResponseDto = UserMapper().toUserResponseDto(createUser())

    fun createJpaUser(id: Long = JPA_ID): JpaUser {
        return JpaUser(
            id = id,
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            phoneNumber = PHONE_NUMBER,
            password = PASSWORD
        )
    }

    fun createMongoUser(id: ObjectId = MONGO_ID): MongoUser {
        return MongoUser(
            id = id,
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            phoneNumber = PHONE_NUMBER,
            password = PASSWORD
        )
    }
}
