package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Author
import com.example.librarymanagement.model.jpa.JpaAuthor
import com.example.librarymanagement.model.mongo.MongoAuthor
import org.bson.types.ObjectId

object AuthorDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val FIRST_NAME = "Test"
    private const val LAST_NAME = "Test"

    fun createAuthor(id: String = ID): Author = Author(id, FIRST_NAME, LAST_NAME)
    fun createJpaAuthor(id: Long = JPA_ID): JpaAuthor = JpaAuthor(id, FIRST_NAME, LAST_NAME)
    fun createMongoAuthor(id: ObjectId = MONGO_ID): MongoAuthor = MongoAuthor(id, FIRST_NAME, LAST_NAME)
}
