package com.example.com.example.gateway.controller.grpc.publisher

import com.example.gateway.publisher.publisher.GetPublisherByIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorGetPublisherByIdServiceGrpc
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdResponse
import reactor.core.publisher.Mono

@GrpcService
class GetPublisherByIdService(
    private val getPublisherByIdNatsPublisher: GetPublisherByIdNatsPublisher,
) : ReactorGetPublisherByIdServiceGrpc.GetPublisherByIdServiceImplBase() {

    override fun getPublisherById(request: Mono<GetPublisherByIdRequest>): Mono<GetPublisherByIdResponse> {
        return request
            .flatMap { publisher ->
                getPublisherByIdNatsPublisher.request(GetPublisherByIdRequest.newBuilder().apply { id = publisher.id }.build())
            }
    }
}
