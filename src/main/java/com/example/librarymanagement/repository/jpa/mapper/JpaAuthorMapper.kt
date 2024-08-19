package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.jpa.JpaAuthor

object JpaAuthorMapper {
    fun toEntity(author: Author): JpaAuthor{
        return JpaAuthor(
                id = author.id?.toLong(),
                firstName = author.firstName,
                lastName = author.lastName
        )
    }

    fun toDomain(jpaAuthor: JpaAuthor): Author{
        return Author(
                id = jpaAuthor.id.toString(),
                firstName = jpaAuthor.firstName,
                lastName = jpaAuthor.lastName
        )
    }
}
