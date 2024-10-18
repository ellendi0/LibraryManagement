package com.example.com.example.gateway.controller.grpc.author

import com.example.gateway.publisher.author.GetAuthorByIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorGetAuthorByIdServiceGrpc
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdRequest
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdResponse
import reactor.core.publisher.Mono

@GrpcService
class GetAuthorByIdService(
    private val getAuthorByIdNatsAuthor: GetAuthorByIdNatsPublisher,
) : ReactorGetAuthorByIdServiceGrpc.GetAuthorByIdServiceImplBase() {

    override fun getAuthorById(request: Mono<GetAuthorByIdRequest>): Mono<GetAuthorByIdResponse> {
        return request
            .flatMap { author ->
                getAuthorByIdNatsAuthor.request(GetAuthorByIdRequest.newBuilder().apply { id = author.id }.build())
            }
    }
}
