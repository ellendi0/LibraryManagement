package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherRequest
import com.example.librarymanagement.dto.PublisherDto
import com.example.librarymanagement.model.domain.Publisher
import org.springframework.stereotype.Component

@Component("natsPublisherMapper")
class PublisherMapper {
    fun toPublisherDto(request: CreatePublisherRequest): PublisherDto {
        return PublisherDto(
            id = request.publisher.id.ifEmpty { null },
            name = request.publisher.name,
        )
    }

    fun toPublisherDto(request: UpdatePublisherRequest): PublisherDto {
        return PublisherDto(
            id = request.publisher.id,
            name = request.publisher.name,
        )
    }

    fun toPublisher(publisherDto: PublisherDto, id: String? = null): Publisher {
        return Publisher(
            id = publisherDto.id ?: id,
            name = publisherDto.name
        )
    }

    fun toPublisherProto(publisher: Publisher): com.example.internalapi.model.Publisher {
        return com.example.internalapi.model.Publisher.newBuilder()
            .setId(publisher.id)
            .setName(publisher.name)
            .build()
    }

}
