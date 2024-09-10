package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.jpa.JpaBookPresence

object JpaBookPresenceMapper {
    fun toEntity(bookPresence: BookPresence): JpaBookPresence {
        return JpaBookPresence(
            id = bookPresence.id?.toLong(),
            availability = bookPresence.availability,
            book = null,
            library = null,
            user = null,
        )
    }

    fun toDomain(jpaBookPresence: JpaBookPresence): BookPresence {
        return BookPresence(
            id = jpaBookPresence.id.toString(),
            availability = jpaBookPresence.availability,
            bookId = jpaBookPresence.book?.id.toString(),
            libraryId = jpaBookPresence.library?.id.toString(),
            userId = jpaBookPresence.user?.id.toString(),
        )
    }
}
