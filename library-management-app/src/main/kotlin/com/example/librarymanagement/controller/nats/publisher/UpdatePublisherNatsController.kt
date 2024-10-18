package com.example.librarymanagement.controller.nats.publisher

import com.example.internalapi.NatsSubject.Publisher.UPDATE
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherResponse
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
        return publisherMapper.toPublisher(request)
            .let { publisherService.updatePublisher(it) }
            .map { publisherMapper.toPublisherProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(publisher: Publisher): UpdatePublisherResponse {
        return UpdatePublisherResponse.newBuilder().apply { successBuilder.setPublisher(publisher) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): UpdatePublisherResponse {
        return UpdatePublisherResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                else -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
