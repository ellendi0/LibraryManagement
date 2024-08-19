package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.dto.UserResponseDto
import com.example.librarymanagement.model.domain.User
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class UserMapper {
    private val salt = BCrypt.gensalt()

    fun toUser(userRequestDto: UserRequestDto, id: String? = null) : User {
        return User(
            id = id,
            firstName = userRequestDto.firstName,
            lastName = userRequestDto.lastName,
            email = userRequestDto.email,
            password = hashPassword(userRequestDto.password),
            phoneNumber = userRequestDto.phoneNumber
        )
    }

    fun toUserResponseDto(user: User) : UserResponseDto {
        return UserResponseDto(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber
        )
    }

    fun toUserResponseDto(user: List<User>) : List<UserResponseDto> = user.map { toUserResponseDto(it) }

    fun hashPassword(password: String): String = BCrypt.hashpw(password, salt)
}
