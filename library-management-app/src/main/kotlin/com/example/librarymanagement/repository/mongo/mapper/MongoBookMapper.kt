package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.mongo.MongoBook
import org.bson.types.ObjectId

object MongoBookMapper {
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
}
