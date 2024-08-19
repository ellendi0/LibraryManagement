package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.AuthorDto
import com.example.librarymanagement.model.domain.Author
import org.springframework.stereotype.Component

@Component
class AuthorMapper {
    fun toAuthor(authorDto: AuthorDto, id: String? = null): Author {
        return Author(
            id = id,
            firstName = authorDto.firstName,
            lastName = authorDto.lastName )
    }

    fun toAuthorDto(author: Author): AuthorDto = AuthorDto(author.id!!, author.firstName, author.lastName)

    fun toAuthorDto(authors: List<Author>): List<AuthorDto> = authors.map { toAuthorDto(it) }
}
