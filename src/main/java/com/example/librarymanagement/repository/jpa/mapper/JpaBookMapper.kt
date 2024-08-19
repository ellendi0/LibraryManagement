package com.example.librarymanagement.repository.jpa.mapper

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.jpa.JpaBook

object JpaBookMapper {
    fun toEntity(book: Book): JpaBook{
        return JpaBook(
                id = book.id?.toLong(),
                title = book.title,
                author = book.author?.let { JpaAuthorMapper.toEntity(it) },
                publisher = book.publisher?.let { JpaPublisherMapper.toEntity(it) },
                publishedYear = book.publishedYear,
                isbn = book.isbn,
                genre = book.genre
        )
    }

    fun toDomain(jpaBook: JpaBook): Book{
        return Book(
                id = jpaBook.id?.toString(),
                title = jpaBook.title,
                author = jpaBook.author?.let { JpaAuthorMapper.toDomain(it) },
                publisher = jpaBook.publisher?.let { JpaPublisherMapper.toDomain(it) },
                publishedYear = jpaBook.publishedYear,
                isbn = jpaBook.isbn,
                genre = jpaBook.genre
        )
    }
}
