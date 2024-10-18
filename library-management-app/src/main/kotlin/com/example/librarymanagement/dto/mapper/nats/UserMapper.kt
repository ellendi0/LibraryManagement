package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.librarymanagement.model.domain.User
import org.springframework.stereotype.Component

@Component("natsUserMapper")
class UserMapper {
    fun toUser(request: CreateUserRequest, id: String? = null): User {
        return User(
            id = request.user.id.ifEmpty { null },
            firstName = request.user.firstName,
            lastName = request.user.lastName,
            email = request.user.email,
            password = request.user.password,
            phoneNumber = request.user.phoneNumber
        )
    }

    fun toUser(request: UpdateUserRequest): User {
        return User(
            id = request.user.id,
            firstName = request.user.firstName,
            lastName = request.user.lastName,
            email = request.user.email,
            password = request.user.password,
            phoneNumber = request.user.phoneNumber,
        )
    }

    fun toUserProto(user: User): UserOutput {
        return UserOutput.newBuilder()
            .setId(user.id)
            .setFirstName(user.firstName)
            .setLastName(user.lastName)
            .setEmail(user.email)
            .setPhoneNumber(user.phoneNumber)
            .build()
    }
}
