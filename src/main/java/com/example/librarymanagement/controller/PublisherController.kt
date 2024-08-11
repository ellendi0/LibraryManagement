package com.example.librarymanagement.controller

import com.example.librarymanagement.dto.PublisherDto
import com.example.librarymanagement.dto.mapper.PublisherMapper
import com.example.librarymanagement.service.PublisherService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/publisher")
class PublisherController(
    private val publisherService: PublisherService,
    private val publisherMapper: PublisherMapper
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllPublishers(): List<PublisherDto> = publisherMapper.toPublisherDto(publisherService.getAllPublishers())

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPublisherById(@PathVariable id: Long): PublisherDto {
        return publisherMapper.toPublisherDto(publisherService.getPublisherById(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPublisher(@RequestBody @Valid publisherDto: PublisherDto): PublisherDto {
        return publisherMapper
            .toPublisherDto(publisherService.createPublisher(publisherMapper.toPublisher(publisherDto)))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePublisher(@PathVariable id: Long, @RequestBody @Valid publisherDto: PublisherDto): PublisherDto {
        return publisherMapper.toPublisherDto(
            publisherService.updatePublisher(publisherMapper.toPublisher(publisherDto, id)
            )
        )
    }
}
