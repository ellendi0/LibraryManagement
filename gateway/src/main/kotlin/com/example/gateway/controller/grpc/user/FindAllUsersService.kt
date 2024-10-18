package com.example.com.example.gateway.controller.grpc.user

import com.example.gateway.publisher.user.FindAllUsersNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllUsersServiceGrpc
import com.example.internalapi.request.user.find_all.proto.FindAllUsersRequest
import com.example.internalapi.request.user.find_all.proto.FindAllUsersResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllUsersService(
    private val findAllUsersNatsUser: FindAllUsersNatsPublisher,
) : ReactorFindAllUsersServiceGrpc.FindAllUsersServiceImplBase() {

    override fun findAllUsers(request: Mono<FindAllUsersRequest>): Mono<FindAllUsersResponse> {
        return request.flatMap { user -> findAllUsersNatsUser.request(user) }
    }
}
