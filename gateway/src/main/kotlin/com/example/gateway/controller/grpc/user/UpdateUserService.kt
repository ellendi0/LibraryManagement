package com.example.com.example.gateway.controller.grpc.user

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.UserRequestValidation
import com.example.gateway.publisher.user.UpdateUserNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorUpdateUserServiceGrpc
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserResponse
import reactor.core.publisher.Mono

@GrpcService
class UpdateUserService(
    private val updateUserNatsUser: UpdateUserNatsPublisher,
    private val validator: UserRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorUpdateUserServiceGrpc.UpdateUserServiceImplBase() {

    override fun updateUser(request: Mono<UpdateUserRequest>): Mono<UpdateUserResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { user -> updateUserNatsUser.request(user) }
            .onErrorResume {
                Mono.just(UpdateUserResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
