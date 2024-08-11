package com.example.librarymanagement.data

import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.model.entity.User

object UserDataFactory {
    fun createUser(): User {
        return User(
            id = 1L,
            firstName = "First",
            lastName = "First",
            email = "first@email.com",
            phoneNumber = "1234567890",
            password = "Password1"
        )
    }

    fun createUserRequestDto(): UserRequestDto {
        return UserRequestDto(
            firstName = "First",
            lastName = "First",
            email = "first@example.com",
            phoneNumber = "1234567890",
            password = "Password1"
        )
    }
}
