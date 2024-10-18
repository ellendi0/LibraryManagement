package com.example.com.example.gateway.controller.grpc.publisher

import com.example.gateway.publisher.publisher.FindAllPublishersNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllPublishersServiceGrpc
import com.example.internalapi.request.publisher.find_all.proto.FindAllPublishersRequest
import com.example.internalapi.request.publisher.find_all.proto.FindAllPublishersResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllPublishersService(
    private val findAllPublishersNatsPublisher: FindAllPublishersNatsPublisher,
) : ReactorFindAllPublishersServiceGrpc.FindAllPublishersServiceImplBase() {

    override fun findAllPublishers(request: Mono<FindAllPublishersRequest>): Mono<FindAllPublishersResponse> {
        return request.flatMap { publisher -> findAllPublishersNatsPublisher.request(publisher) }
    }
}
