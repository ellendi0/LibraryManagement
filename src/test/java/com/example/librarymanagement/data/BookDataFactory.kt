package com.example.librarymanagement.data

import com.example.librarymanagement.dto.BookDto
import com.example.librarymanagement.model.domain.Book
import com.example.librarymanagement.model.enums.Genre
import com.example.librarymanagement.model.jpa.JpaBook
import com.example.librarymanagement.model.mongo.MongoBook
import org.bson.types.ObjectId

object BookDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val TITLE = "Test"
    private const val PUBLISHED_YEAR = 2020
    private val GENRE = Genre.DRAMA
    private const val ISBN = 1234567890123L

    fun createBook(id: String = ID): Book {
        return Book(
            id = id,
            title = TITLE,
            authorId = id,
            publisherId = id,
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }

    fun createBookRequestDto(id: String = ID): BookDto {
        return BookDto(
            id = id,
            title = TITLE,
            authorId = id,
            publisherId = id,
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }

    fun createJpaBook(id: Long = JPA_ID): JpaBook {
        return JpaBook(
            id = id,
            title = TITLE,
            publisher = null,
            author = null,
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }

    fun createMongoBook(id: ObjectId = MONGO_ID): MongoBook {
        return MongoBook(
            id = id,
            title = TITLE,
            authorId = id,
            publisherId = id,
            publishedYear = PUBLISHED_YEAR,
            genre = GENRE,
            isbn = ISBN
        )
    }
}
