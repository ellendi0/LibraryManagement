package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.jpa.JpaLibrary
import com.example.librarymanagement.model.mongo.MongoLibrary
import org.bson.types.ObjectId

object LibraryDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val NAME = "Test"
    private const val ADDRESS = "Test"

    fun createLibrary(id: Any) = Library(id.toString(), NAME, ADDRESS)
    fun createJpaLibrary() = JpaLibrary(JPA_ID, NAME, ADDRESS)
    fun createMongoLibrary() = MongoLibrary(MONGO_ID, NAME, ADDRESS)
}
