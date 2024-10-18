package com.example.com.example.gateway.controller.grpc.publisher

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.PublisherRequestValidation
import com.example.gateway.publisher.publisher.CreatePublisherNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorCreatePublisherServiceGrpc
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse
import reactor.core.publisher.Mono

@GrpcService
class CreatePublisherService(
    private val createPublisherNatsPublisher: CreatePublisherNatsPublisher,
    private val validator: PublisherRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorCreatePublisherServiceGrpc.CreatePublisherServiceImplBase() {

    override fun createPublisher(request: Mono<CreatePublisherRequest>): Mono<CreatePublisherResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { publisher -> createPublisherNatsPublisher.request(publisher) }
            .onErrorResume {
                Mono.just(CreatePublisherResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
