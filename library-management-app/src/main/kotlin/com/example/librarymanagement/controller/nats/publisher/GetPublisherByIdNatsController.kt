package com.example.librarymanagement.controller.nats.publisher

import com.example.internalapi.NatsSubject.Publisher.GET_BY_ID
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.PublisherMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.PublisherService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetPublisherByIdNatsController(
    private val publisherService: PublisherService,
    private val publisherMapper: PublisherMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetPublisherByIdRequest, GetPublisherByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetPublisherByIdRequest> = GetPublisherByIdRequest.parser()

    override fun handle(request: GetPublisherByIdRequest): Mono<GetPublisherByIdResponse> {
        return publisherService.getPublisherById(request.id)
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
//        val errorProto = if (exception is EntityNotFoundException) {
//            errorMapper.toErrorProto(exception)
//        } else {
//            // Handle generic exceptions or other exception types
//            errorMapper.toErrorProto(EntityNotFoundException("Unexpected error occurred"))
//        }
//
//        return GetPublisherByIdResponse.newBuilder().apply { failureBuilder.setNotFoundError(errorProto) }
//            .build()
    }

}
