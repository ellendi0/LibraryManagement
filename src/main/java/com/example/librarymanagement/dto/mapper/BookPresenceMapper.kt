package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.BookPresenceDto
import com.example.librarymanagement.model.domain.BookPresence
import org.springframework.stereotype.Component

@Component
class BookPresenceMapper {
    fun toBookPresenceDto(bookPresence: BookPresence): BookPresenceDto {
        return BookPresenceDto(
            id = bookPresence.id,
            bookId = bookPresence.bookId,
            libraryId = bookPresence.libraryId,
            userId = bookPresence.userId,
            availability = bookPresence.availability
        )
    }

    fun toBookPresenceDto(bookPresenceList: List<BookPresence?>): List<BookPresenceDto> {
        return bookPresenceList.map { toBookPresenceDto(it!!) }
    }
}
