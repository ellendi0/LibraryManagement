package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.jpa.JpaBookPresence

object JpaBookPresenceMapper {
    fun toEntity(bookPresence: BookPresence): JpaBookPresence{
        return JpaBookPresence(
                id = bookPresence.id?.toLong(),
                availability = bookPresence.availability,
                book = JpaBookMapper.toEntity(bookPresence.book),
                library = JpaLibraryMapper.toEntity(bookPresence.library),
                user = bookPresence.user?.let { JpaUserMapper.toEntity(it) }
        )
    }

    fun toDomain(jpaBookPresence: JpaBookPresence): BookPresence{
        return BookPresence(
                id = jpaBookPresence.id.toString(),
                availability = jpaBookPresence.availability,
                book = JpaBookMapper.toDomain(jpaBookPresence.book),
                library = JpaLibraryMapper.toDomain(jpaBookPresence.library),
                user = jpaBookPresence.user?.let { JpaUserMapper.toDomain(it) }
        )
    }
}
