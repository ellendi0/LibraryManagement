package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.enums.Genre
import org.springframework.stereotype.Component

@Component("natsBookMapper")
class BookMapper {
    fun toBook(request: CreateBookRequest, id: String? = null): Book {
        return Book(
            id = request.book.id.ifEmpty { null },
            title = request.book.title,
            authorId = request.book.authorId,
            publisherId = request.book.publisherId,
            publishedYear = request.book.publishedYear,
            isbn = request.book.isbn,
            genre = Genre.valueOf(request.book.genre.name)
        )
    }

    fun toBook(request: UpdateBookRequest): Book {
        return Book(
            id = request.book.id,
            title = request.book.title,
            authorId = request.book.authorId,
            publisherId = request.book.publisherId,
            publishedYear = request.book.publishedYear,
            isbn = request.book.isbn,
            genre = Genre.valueOf(request.book.genre.name)
        )
    }

    fun toBookProto(book: Book): com.example.internalapi.model.Book {
        return com.example.internalapi.model.Book.newBuilder()
            .setId(book.id)
            .setTitle(book.title)
            .setAuthorId(book.authorId)
            .setPublisherId(book.publisherId)
            .setPublishedYear(book.publishedYear)
            .setIsbn(book.isbn)
            .setGenre(com.example.internalapi.model.Genre.valueOf(book.genre.toString()))
            .build()
    }
}
