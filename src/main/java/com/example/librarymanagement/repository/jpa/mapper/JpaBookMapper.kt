package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.jpa.JpaBook

object JpaBookMapper {
    fun toEntity(book: Book): JpaBook {
        return JpaBook(
            id = book.id?.toLong(),
            title = book.title,
            publishedYear = book.publishedYear,
            author = null,
            publisher = null,
            isbn = book.isbn,
            genre = book.genre
        )
    }

    fun toDomain(jpaBook: JpaBook): Book {
        return Book(
            id = jpaBook.id.toString(),
            title = jpaBook.title,
            authorId = jpaBook.author?.id.toString(),
            publisherId = jpaBook.publisher?.id.toString(),
            publishedYear = jpaBook.publishedYear,
            isbn = jpaBook.isbn,
            genre = jpaBook.genre
        )
    }
}
