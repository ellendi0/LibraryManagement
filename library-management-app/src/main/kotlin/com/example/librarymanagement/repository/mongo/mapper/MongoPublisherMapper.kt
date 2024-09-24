package com.example.librarymanagement.repository.mongo.mapper

import com.example.librarymanagement.model.domain.Publisher
import com.example.librarymanagement.model.mongo.MongoPublisher
import org.bson.types.ObjectId

object MongoPublisherMapper {
    fun toEntity(publisher: Publisher): MongoPublisher {
        return MongoPublisher(
                id = publisher.id?.let { ObjectId(it) },
                name = publisher.name
        )
    }

    fun toDomain(mongoPublisher: MongoPublisher): Publisher{
        return Publisher(
                id = mongoPublisher.id?.toString(),
                name = mongoPublisher.name
        )
    }
}
