package com.example.com.example.gateway.controller.grpc.user

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.UserRequestValidation
import com.example.gateway.publisher.user.CreateUserNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorCreateUserServiceGrpc
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.create.proto.CreateUserResponse
import reactor.core.publisher.Mono

@GrpcService
class CreateUserService(
    private val createUserNatsPublisher: CreateUserNatsPublisher,
    private val validator: UserRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorCreateUserServiceGrpc.CreateUserServiceImplBase() {

    override fun createUser(request: Mono<CreateUserRequest>): Mono<CreateUserResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { user -> createUserNatsPublisher.request(user) }
            .onErrorResume {
                Mono.just(CreateUserResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
