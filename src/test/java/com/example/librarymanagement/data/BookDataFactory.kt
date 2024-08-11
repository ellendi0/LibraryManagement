package com.example.librarymanagement.data

import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.model.enums.Genre

object BookDataFactory {
    fun createBook(): Book {
        val author = AuthorDataFactory.createAuthor()
        val publisher = PublisherDataFactory.createPublisher()
        return Book(
            id = 1L,
            title = "Book",
            author = author,
            publisher = publisher,
            publishedYear = 2021,
            genre = Genre.DRAMA,
            isbn = 1234567890123)
    }

    fun createBookRequestDto(): BookRequestDto {
        return BookRequestDto(
            title = "Book",
            authorId = 1L,
            publisherId = 1L,
            publishedYear = 2021,
            genre = Genre.DRAMA,
            isbn = 1234567890123L
        )
    }
}
