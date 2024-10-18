package com.example.librarymanagement.user.infrastructure.convertor

import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import com.example.librarymanagement.user.domain.User
import com.example.librarymanagement.user.infrastructure.entity.MongoUser

@Component
class UserMapper {
    fun toEntity(user: User): MongoUser {
        return MongoUser(
            id = user.id?.let { ObjectId(it) },
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = user.password,
            phoneNumber = user.phoneNumber
        )
    }

    fun toDomain(mongoUser: MongoUser): User {
        return User(
            id = mongoUser.id.toString(),
            firstName = mongoUser.firstName,
            lastName = mongoUser.lastName,
            email = mongoUser.email,
            password = mongoUser.password,
            phoneNumber = mongoUser.phoneNumber
        )
    }

    fun toUser(request: CreateUserRequest): User {
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

