package com.example.librarymanagement.book.infrastructure.convertor

import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.librarymanagement.book.domain.Book
import com.example.librarymanagement.book.domain.Genre
import com.example.librarymanagement.book.infrastructure.entity.MongoBook
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class BookMapper {
    fun toEntity(book: Book): MongoBook{
        return MongoBook(
            id = book.id?.let { ObjectId(it) },
            title = book.title,
            publishedYear = book.publishedYear,
            authorId = ObjectId(book.authorId),
            publisherId = ObjectId(book.publisherId),
            isbn = book.isbn,
            genre = book.genre
        )
    }

    fun toDomain(mongoBook: MongoBook): Book{
        return Book(
            id = mongoBook.id.toString(),
            title = mongoBook.title,
            authorId = mongoBook.authorId.toString(),
            publisherId = mongoBook.publisherId.toString(),
            publishedYear = mongoBook.publishedYear,
            isbn = mongoBook.isbn,
            genre = mongoBook.genre
        )
    }

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
