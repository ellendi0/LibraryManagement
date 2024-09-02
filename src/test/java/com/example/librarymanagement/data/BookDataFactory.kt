package com.example.librarymanagement.data

import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.enums.Genre
import com.example.librarymanagement.model.jpa.JpaBook
import com.example.librarymanagement.model.mongo.MongoBook
import org.bson.types.ObjectId

object BookDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val TITLE = "Test"
    private const val PUBLISHED_YEAR = 2020
    private val GENRE = Genre.DRAMA
    private const val ISBN = 1234567890123L

    fun createBook(id: Any): Book {
        return Book(
            id = id.toString(),
            title = TITLE,
            authorId = id.toString(),
            publisherId = id.toString(),
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }

    fun createBookRequestDto(id: Any): BookRequestDto {
        return BookRequestDto(
            title = TITLE,
            authorId = id.toString(),
            publisherId = id.toString(),
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }

    fun createJpaBook(): JpaBook {
        return JpaBook(
            id = JPA_ID,
            title = TITLE,
            publisher = null,
            author = null,
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }

    fun createMongoBook(): MongoBook {
        return MongoBook(
            id = MONGO_ID,
            title = TITLE,
            authorId = MONGO_ID,
            publisherId = MONGO_ID,
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }
}
