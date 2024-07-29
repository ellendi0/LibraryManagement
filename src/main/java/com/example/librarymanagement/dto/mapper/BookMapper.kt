package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.dto.BookResponseDto
import com.example.librarymanagement.model.entity.Book
import org.springframework.stereotype.Component

@Component
class BookMapper {
    fun toBook(bookRequestDto: BookRequestDto): Book {
        return Book(
            title = bookRequestDto.title,
            isbn = bookRequestDto.isbn,
            publishedYear = bookRequestDto.publishedYear,
            genre = bookRequestDto.genre
        )
    }

    fun toBookDto(book: Book): BookResponseDto {
        val author = book.author!!

        return BookResponseDto(
            id = book.id!!,
            title = book.title,
            author = "${author.firstName} ${author.lastName}",
            publisher = book.publisher!!.name,
            publishedYear = book.publishedYear,
            isbn = book.isbn,
            genre = book.genre
        )
    }

    fun toBookDto(books: List<Book>): List<BookResponseDto> = books.map { toBookDto(it) }
}