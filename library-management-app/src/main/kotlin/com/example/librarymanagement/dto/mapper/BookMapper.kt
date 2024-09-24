package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.BookDto
import com.example.librarymanagement.model.domain.Book
import org.springframework.stereotype.Component

@Component
class BookMapper {
    fun toBook(bookDto: BookDto, id: String? = null): Book {
        return Book(
            id = bookDto.id ?: id,
            title = bookDto.title,
            authorId = bookDto.authorId,
            publisherId = bookDto.publisherId,
            isbn = bookDto.isbn,
            publishedYear = bookDto.publishedYear,
            genre = bookDto.genre,
        )
    }

    fun toBookDto(book: Book): BookDto {
        return BookDto(
            id = book.id,
            title = book.title,
            authorId = book.authorId,
            publisherId = book.publisherId,
            publishedYear = book.publishedYear,
            isbn = book.isbn,
            genre = book.genre
        )
    }

    fun toBookDto(books: List<Book>): List<BookDto> = books.map { toBookDto(it) }
}
