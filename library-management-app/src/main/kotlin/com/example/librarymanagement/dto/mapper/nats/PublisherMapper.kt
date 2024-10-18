package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import com.example.librarymanagement.model.domain.Publisher
import org.springframework.stereotype.Component
import com.example.internalapi.model.Publisher as PublisherProto

@Component("natsPublisherMapper")
class PublisherMapper {
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
