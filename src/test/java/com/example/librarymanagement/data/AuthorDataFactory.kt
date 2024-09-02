package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.jpa.JpaAuthor
import com.example.librarymanagement.model.mongo.MongoAuthor
import org.bson.types.ObjectId

object AuthorDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val FIRST_NAME = "Test"
    private const val LAST_NAME = "Test"

    fun createAuthor(id: Any): Author = Author(id.toString(), FIRST_NAME, LAST_NAME)
    fun createJpaAuthor(): JpaAuthor = JpaAuthor(JPA_ID, FIRST_NAME, LAST_NAME)
    fun createMongoAuthor(): MongoAuthor = MongoAuthor(MONGO_ID, FIRST_NAME, LAST_NAME)
}
