package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.BookPresenceDto
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.jpa.JpaBookPresence
import org.springframework.stereotype.Component

@Component
class BookPresenceMapper(private val userMapper: UserMapper) {
    fun toBookPresenceDto(bookPresence: BookPresence): BookPresenceDto {
        return BookPresenceDto(
            id = bookPresence.id.toString(),
            bookTitle = bookPresence.book.title,
            bookAuthorId = bookPresence.book.author!!.id!!,
            libraryId = bookPresence.library.id!!,
            user = bookPresence.user?.let { userMapper.toUserResponseDto(it) },
            availability = bookPresence.availability
        )
    }

    fun toBookPresenceDto(bookPresenceList: List<BookPresence?>): List<BookPresenceDto> {
        return bookPresenceList.map { toBookPresenceDto(it!!) }
    }
}
