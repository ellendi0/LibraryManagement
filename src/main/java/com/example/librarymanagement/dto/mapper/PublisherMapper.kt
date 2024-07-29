package com.example.librarymanagement.dto.mapper

import com.example.librarymanagement.dto.PublisherDto
import com.example.librarymanagement.model.entity.Publisher
import org.springframework.stereotype.Component

@Component
class PublisherMapper {
    fun toPublisher(publisherDto: PublisherDto): Publisher = Publisher(name = publisherDto.name)
    fun toPublisherDto(publisher: Publisher): PublisherDto = PublisherDto(publisher.id!!, publisher.name)
    fun toPublisherDto(publishers: List<Publisher>): List<PublisherDto> = publishers.map { toPublisherDto(it) }
}