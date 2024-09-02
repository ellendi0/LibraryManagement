package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.dto.BookResponseDto
import com.example.librarymanagement.model.domain.Book
import org.springframework.stereotype.Component

@Component
class BookMapper {
    fun toBook(bookRequestDto: BookRequestDto, id: String? = null): Book {
        return Book(
            id = id,
            title = bookRequestDto.title,
            authorId = bookRequestDto.authorId,
            publisherId = bookRequestDto.publisherId,
            isbn = bookRequestDto.isbn,
            publishedYear = bookRequestDto.publishedYear,
            genre = bookRequestDto.genre,
        )
    }

    fun toBookDto(book: Book): BookResponseDto {
        return BookResponseDto(
            id = book.id!!,
            title = book.title,
            authorId = book.authorId.toString(),
            publisherId = book.publisherId.toString(),
            publishedYear = book.publishedYear,
            isbn = book.isbn,
            genre = book.genre
        )
    }

    fun toBookDto(books: List<Book>): List<BookResponseDto> = books.map { toBookDto(it) }
}
