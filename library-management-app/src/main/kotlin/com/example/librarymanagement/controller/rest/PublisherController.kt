package com.example.librarymanagement.controller.rest

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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/publisher")
class PublisherController(
    private val publisherService: PublisherService,
    private val publisherMapper: PublisherMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllPublishers(): Flux<PublisherDto> =
        publisherService.getAllPublishers().map { publisherMapper.toPublisherDto(it) }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPublisherById(@PathVariable id: String): Mono<PublisherDto> {
        return publisherService.getPublisherById(id).map { publisherMapper.toPublisherDto(it) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPublisher(@RequestBody @Valid publisherDto: PublisherDto): Mono<PublisherDto> {
        return publisherService
            .createPublisher(publisherMapper.toPublisher(publisherDto))
            .map { publisherMapper.toPublisherDto(it) }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePublisher(@PathVariable id: String, @RequestBody @Valid publisherDto: PublisherDto): Mono<PublisherDto> {
        return publisherService
            .updatePublisher(publisherMapper.toPublisher(publisherDto, id))
            .map { publisherMapper.toPublisherDto(it) }
    }
}
