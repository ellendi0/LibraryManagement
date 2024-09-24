package com.example.librarymanagement.model.mongo

import com.example.librarymanagement.model.enums.Genre
import com.example.librarymanagement.model.mongo.MongoBook.Companion.COLLECTION_NAME
import jakarta.persistence.Id
import org.bson.types.ObjectId
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
//mongook creation of index
