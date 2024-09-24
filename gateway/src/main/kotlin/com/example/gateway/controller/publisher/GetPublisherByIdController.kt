package com.example.com.example.gateway.controller.publisher

import com.example.com.example.gateway.exception.EntityNotFoundException
import com.example.com.example.gateway.publisher.publisher.GetPublisherByIdNatsPublisher
import com.example.gateway.model.PublisherDto
import com.example.gateway.model.mapper.PublisherMapper
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdResponse
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdResponse.Failure
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v2/publisher")
class GetPublisherByIdController(
    private val getPublisherByIdNatsPublisher: GetPublisherByIdNatsPublisher,
    private val publisherMapper: PublisherMapper
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPublisherById(@PathVariable id: String): Mono<PublisherDto> {
        return getPublisherByIdNatsPublisher.request(GetPublisherByIdRequest.newBuilder().setId(id).build())
            .flatMap { response ->
                if (response.hasSuccess()) {
                    Mono.just(handleSuccess(response))
                } else {
                    Mono.error(handleFailure(response.failure))
                }
            }
    }

    private fun handleSuccess(response: GetPublisherByIdResponse): PublisherDto {
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
