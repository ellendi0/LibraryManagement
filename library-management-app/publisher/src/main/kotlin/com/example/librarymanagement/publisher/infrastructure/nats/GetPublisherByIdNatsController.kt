package com.example.librarymanagement.publisher.infrastructure.nats

import com.example.internalapi.NatsSubject.Publisher.GET_BY_ID
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdResponse
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.publisher.application.port.`in`.GetPublisherByIdInPort
import com.example.librarymanagement.publisher.infrastructure.convertor.PublisherMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetPublisherByIdNatsController(
    private val getPublisherByIdInPort: GetPublisherByIdInPort,
    private val publisherMapper: PublisherMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetPublisherByIdRequest, GetPublisherByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetPublisherByIdRequest> = GetPublisherByIdRequest.parser()

    override fun handle(request: GetPublisherByIdRequest): Mono<GetPublisherByIdResponse> {
        return getPublisherByIdInPort.getPublisherById(request.id)
            .map { publisherMapper.toPublisherProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(publisher: Publisher): GetPublisherByIdResponse {
        return GetPublisherByIdResponse.newBuilder().apply { successBuilder.setPublisher(publisher) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): GetPublisherByIdResponse {
        return when (exception) {
            is EntityNotFoundException -> {
                GetPublisherByIdResponse.newBuilder().apply {
                    failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
                }.build()
            }

            is IllegalArgumentException -> {
                GetPublisherByIdResponse.newBuilder().apply {
                    failureBuilder.setIllegalArgumentExpression(errorMapper.toErrorProto(exception))
                }.build()
            }

            else -> {
                GetPublisherByIdResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
                }.build()
            }
        }
    }
}
