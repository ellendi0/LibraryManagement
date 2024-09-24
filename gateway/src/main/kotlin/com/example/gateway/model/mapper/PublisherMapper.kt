package com.example.gateway.model.mapper

import com.example.gateway.model.PublisherDto
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherRequest
import org.springframework.stereotype.Component

@Component
class PublisherMapper {
    fun toPublisherRequest(publisherDto: PublisherDto, id: String? = ""): CreatePublisherRequest {
        return CreatePublisherRequest.newBuilder().setPublisher(toPublisher(publisherDto, id)).build()
    }

    fun toUpdatePublisherRequest(publisherDto: PublisherDto): UpdatePublisherRequest {
        return UpdatePublisherRequest.newBuilder().setPublisher(toPublisher(publisherDto)).build()
    }

    fun toPublisherDto(publisher: Publisher): PublisherDto =
        PublisherDto(publisher.id, publisher.name)

    fun toPublisher(publisherDto: PublisherDto, id: String? = ""): Publisher =
        Publisher.newBuilder()
            .setId(publisherDto.id ?: id )
            .setName(publisherDto.name)
            .build()
}
