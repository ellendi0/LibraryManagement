package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.jpa.JpaLibrary
import com.example.librarymanagement.model.mongo.MongoLibrary
import org.bson.types.ObjectId

object LibraryDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val NAME = "Test"
    private const val ADDRESS = "Test"

    fun createLibrary(id: String = ID) = Library(id, NAME, ADDRESS)
    fun createJpaLibrary(id: Long = JPA_ID) = JpaLibrary(id, NAME, ADDRESS)
    fun createMongoLibrary(id: ObjectId = MONGO_ID) = MongoLibrary(id, NAME, ADDRESS)
}
