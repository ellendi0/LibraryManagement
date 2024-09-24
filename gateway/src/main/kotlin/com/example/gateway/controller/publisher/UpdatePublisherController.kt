package com.example.com.example.gateway.controller.publisher

import com.example.com.example.gateway.exception.EntityNotFoundException
import com.example.com.example.gateway.publisher.publisher.UpdatePublisherNatsPublisher
import com.example.gateway.model.PublisherDto
import com.example.gateway.model.mapper.PublisherMapper
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherResponse
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherResponse.Failure
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v2/publisher")
class UpdatePublisherController(
    private val updatePublisherNatsPublisher: UpdatePublisherNatsPublisher,
    private val publisherMapper: PublisherMapper
) {
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updatePublisher(@RequestBody @Valid publisherDto: PublisherDto): Mono<PublisherDto> {
        return publisherMapper.toUpdatePublisherRequest(publisherDto)
            .let { updatePublisherNatsPublisher.request(it) }
            .flatMap { request ->
                if (request.hasSuccess()) {
                    Mono.just(handleSuccess(request))
                } else {
                    Mono.error(handleFailure(request.failure))
                }
            }
    }

    private fun handleSuccess(response: UpdatePublisherResponse): PublisherDto {
        return publisherMapper.toPublisherDto(response.success.publisher)
    }

    private fun handleFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.NOT_FOUND_ERROR -> EntityNotFoundException(failure.notFoundError.messages)
            Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }
}
