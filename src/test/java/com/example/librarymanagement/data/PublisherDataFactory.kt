package com.example.librarymanagement.data

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.jpa.JpaPublisher
import com.example.librarymanagement.model.mongo.MongoPublisher
import org.bson.types.ObjectId

object PublisherDataFactory {
    const val JPA_ID = 1L
    val MONGO_ID = ObjectId("111111111111111111111111")
    private const val NAME = "Test"

    fun createPublisher(id: Any) = Publisher(id.toString(), NAME)
    fun createJpaPublisher() = JpaPublisher(JPA_ID, NAME)
    fun createMongoPublisher() = MongoPublisher(MONGO_ID, NAME)
}
