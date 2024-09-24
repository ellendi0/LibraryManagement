package com.example.librarymanagement.controller.nats.publisher

import com.example.internalapi.NatsSubject.Publisher.UPDATE
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.PublisherMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.PublisherService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdatePublisherNatsController(
    private val publisherService: PublisherService,
    private val publisherMapper: PublisherMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdatePublisherRequest, UpdatePublisherResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdatePublisherRequest> = UpdatePublisherRequest.parser()

    override fun handle(request: UpdatePublisherRequest): Mono<UpdatePublisherResponse> {
        return publisherMapper.toPublisherDto(request)
            .let { publisherMapper.toPublisher(it) }
            .let { publisherService.updatePublisher(it) }
            .map { publisherMapper.toPublisherProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(publisher: Publisher): UpdatePublisherResponse {
        return UpdatePublisherResponse.newBuilder().apply { successBuilder.setPublisher(publisher) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): UpdatePublisherResponse {
        return when (exception) {
            is EntityNotFoundException -> {
                UpdatePublisherResponse.newBuilder().apply {
                    failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
                }.build()
            }

            is IllegalArgumentException -> {
                UpdatePublisherResponse.newBuilder().apply {
                    failureBuilder.setIllegalArgumentExpression(errorMapper.toErrorProto(exception))
                }.build()
            }

            else -> {
                UpdatePublisherResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
                }.build()
            }
        }
    }
}
