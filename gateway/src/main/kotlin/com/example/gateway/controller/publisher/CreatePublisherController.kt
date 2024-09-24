package com.example.com.example.gateway.controller.publisher

import com.example.gateway.model.PublisherDto
import com.example.gateway.model.mapper.PublisherMapper
import com.example.gateway.publisher.publisher.CreatePublisherNatsPublisher
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse.Failure
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v2/publisher")
class CreatePublisherController(
    private val createPublisher: CreatePublisherNatsPublisher,
    private val publisherMapper: PublisherMapper
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPublisher(@RequestBody @Valid publisherDto: PublisherDto): Mono<PublisherDto> {
        return publisherMapper.toPublisherRequest(publisherDto)
            .let { createPublisher.request(it)}
            .flatMap { request ->
                if (request.hasSuccess()) {
                    Mono.just(handleSuccess(request))
                } else {
                    Mono.error(handleFailure(request.failure))
                }
            }
    }

    private fun handleSuccess(response: CreatePublisherResponse): PublisherDto {
        return publisherMapper.toPublisherDto(response.success.publisher)
    }

    private fun handleFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }
}
