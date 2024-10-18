package com.example.com.example.gateway.controller.grpc.publisher

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.PublisherRequestValidation
import com.example.gateway.publisher.publisher.UpdatePublisherNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorUpdatePublisherServiceGrpc
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherResponse
import reactor.core.publisher.Mono

@GrpcService
class UpdatePublisherService(
    private val updatePublisherNatsPublisher: UpdatePublisherNatsPublisher,
    private val validator: PublisherRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorUpdatePublisherServiceGrpc.UpdatePublisherServiceImplBase() {

    override fun updatePublisher(request: Mono<UpdatePublisherRequest>): Mono<UpdatePublisherResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { publisher -> updatePublisherNatsPublisher.request(publisher) }
            .onErrorResume {
                Mono.just(UpdatePublisherResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
