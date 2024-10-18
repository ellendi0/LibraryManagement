package com.example.com.example.gateway.controller.grpc.user

import com.example.gateway.publisher.user.GetUserByIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorGetUserByIdServiceGrpc
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdRequest
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdResponse
import reactor.core.publisher.Mono

@GrpcService
class GetUserByIdService(
    private val getUserByIdNatsUser: GetUserByIdNatsPublisher,
) : ReactorGetUserByIdServiceGrpc.GetUserByIdServiceImplBase() {

    override fun getUserById(request: Mono<GetUserByIdRequest>): Mono<GetUserByIdResponse> {
        return request
            .flatMap { user ->
                getUserByIdNatsUser.request(GetUserByIdRequest.newBuilder().apply { id = user.id }.build())
            }
    }
}
