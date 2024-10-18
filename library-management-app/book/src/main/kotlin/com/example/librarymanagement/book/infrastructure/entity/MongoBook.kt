package com.example.librarymanagement.book.infrastructure.entity

import com.example.librarymanagement.book.domain.Genre
import com.example.librarymanagement.book.infrastructure.entity.MongoBook.Companion.COLLECTION_NAME
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = COLLECTION_NAME)
data class MongoBook(
    @Id val id: ObjectId? = null,
    val title: String,
    val authorId: ObjectId?,
    val publisherId: ObjectId?,
    val publishedYear: Int,

    @Indexed(unique = true)
    val isbn: Long,
    val genre: Genre
) {
    companion object{
        const val COLLECTION_NAME = "book"
    }
}
