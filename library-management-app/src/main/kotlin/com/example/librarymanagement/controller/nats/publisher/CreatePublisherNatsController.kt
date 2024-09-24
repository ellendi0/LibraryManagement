package com.example.librarymanagement.controller.nats.publisher

import com.example.internalapi.NatsSubject.Publisher.CREATE
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.PublisherMapper
import com.example.librarymanagement.service.PublisherService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreatePublisherNatsController(
    private val publisherService: PublisherService,
    private val publisherMapper: PublisherMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreatePublisherRequest, CreatePublisherResponse> {
    override val subject = CREATE
    override val parser: Parser<CreatePublisherRequest> = CreatePublisherRequest.parser()

    override fun handle(request: CreatePublisherRequest): Mono<CreatePublisherResponse> {
        return publisherMapper.toPublisherDto(request)
            .let { publisherMapper.toPublisher(it) }
            .let { publisherService.createPublisher(it) }
            .map { publisherMapper.toPublisherProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(publisher: Publisher): CreatePublisherResponse {
        return CreatePublisherResponse.newBuilder().apply { successBuilder.setPublisher(publisher) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): CreatePublisherResponse {
        return CreatePublisherResponse.newBuilder().apply {
            failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
        }.build()
    }
}
