package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.AuthorDto
import com.example.librarymanagement.model.entity.Author
import org.springframework.stereotype.Component

@Component
class AuthorMapper {
    fun toAuthor(authorDto: AuthorDto): Author {
        return Author(
            firstName = authorDto.firstName,
            lastName = authorDto.lastName,
        )
    }

    fun toAuthorDto(author: Author): AuthorDto {
        return AuthorDto(author.id, author.firstName, author.lastName)
    }

    fun toAuthorDto(authors: List<Author>): List<AuthorDto> {
        return authors.map { toAuthorDto(it) }
    }
}