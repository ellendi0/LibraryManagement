package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.jpa.JpaPublisher
import com.example.librarymanagement.model.mongo.MongoPublisher
import org.bson.types.ObjectId

object PublisherDataFactory {
    const val JPA_ID = 1L
    const val ID = "1"
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val NAME = "Test"

    fun createPublisher(id: String = ID) = Publisher(id, NAME)
    fun createJpaPublisher(id: Long = JPA_ID) = JpaPublisher(id, NAME)
    fun createMongoPublisher(id: ObjectId = MONGO_ID) = MongoPublisher(id, NAME)
}
