package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.PublisherDto
import com.example.librarymanagement.model.domain.Publisher
import org.springframework.stereotype.Component

@Component
class PublisherMapper {
    fun toPublisher(publisherDto: PublisherDto, id: String? = null): Publisher {
        return Publisher(id = id, name = publisherDto.name)
    }

    fun toPublisherDto(publisher: Publisher): PublisherDto = PublisherDto(publisher.id!!, publisher.name)

    fun toPublisherDto(publishers: List<Publisher>): List<PublisherDto> = publishers.map { toPublisherDto(it) }
}
