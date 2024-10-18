package com.example.librarymanagement.publisher.infrastructure.convertor

import com.example.internalapi.model.Publisher as PublisherProto
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import com.example.librarymanagement.publisher.domain.Publisher
import com.example.librarymanagement.publisher.infrastructure.entity.MongoPublisher
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class PublisherMapper {
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

    fun toPublisher(request: CreatePublisherRequest): Publisher {
        return Publisher(
            id = request.publisher.id.ifEmpty { null },
            name = request.publisher.name,
        )
    }

    fun toPublisher(request: UpdatePublisherRequest): Publisher {
        return Publisher(
            id = request.publisher.id,
            name = request.publisher.name,
        )
    }

    fun toPublisherProto(publisher: Publisher): PublisherProto {
        return PublisherProto.newBuilder()
            .setId(publisher.id)
            .setName(publisher.name)
            .build()
    }
}
