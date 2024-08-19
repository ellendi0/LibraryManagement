package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.User
import com.example.librarymanagement.model.jpa.JpaUser

object JpaUserMapper {
    fun toEntity(user: User): JpaUser{
        return JpaUser(
                id = user.id?.toLong(),
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                password = user.password,
                phoneNumber = user.phoneNumber
        )
    }

    fun toDomain(jpaUser: JpaUser): User{
        return User(
                id = jpaUser.id.toString(),
                firstName = jpaUser.firstName,
                lastName = jpaUser.lastName,
                email = jpaUser.email,
                password = jpaUser.password,
                phoneNumber = jpaUser.phoneNumber
        )
    }
}
