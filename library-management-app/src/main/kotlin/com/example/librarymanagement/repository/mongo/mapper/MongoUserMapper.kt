package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.mongo.MongoUser
import org.bson.types.ObjectId

object MongoUserMapper {
    fun toEntity(user: User): MongoUser{
        return MongoUser(
                id = user.id?.let { ObjectId(it) },
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                password = user.password,
                phoneNumber = user.phoneNumber
        )
    }

    fun toDomain(mongoUser: MongoUser): User{
        return User(
                id = mongoUser.id.toString(),
                firstName = mongoUser.firstName,
                lastName = mongoUser.lastName,
                email = mongoUser.email,
                password = mongoUser.password,
                phoneNumber = mongoUser.phoneNumber
        )
    }
}
