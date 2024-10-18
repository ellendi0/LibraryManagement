package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.UserRequestValidation
import com.example.gateway.publisher.user.CreateUserNatsPublisher
import com.example.gateway.publisher.user.GetAllUsersNatsPublisher
import com.example.gateway.publisher.user.GetUserByIdNatsPublisher
import com.example.gateway.publisher.user.UpdateUserNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorUserServiceGrpc
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.create.proto.CreateUserResponse
import com.example.internalapi.request.user.get_all.proto.GetAllUsersRequest
import com.example.internalapi.request.user.get_all.proto.GetAllUsersResponse
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdRequest
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdResponse
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserResponse
import reactor.core.publisher.Mono

@GrpcService
class UserService(
    private val createUserNatsUser: CreateUserNatsPublisher,
    private val getAllUsersNatsUser: GetAllUsersNatsPublisher,
    private val getUserByIdNatsUser: GetUserByIdNatsPublisher,
    private val updateUserNatsUser: UpdateUserNatsPublisher,
    private val validator: UserRequestValidation
) : ReactorUserServiceGrpc.UserServiceImplBase() {

    override fun createUser(request: Mono<CreateUserRequest>): Mono<CreateUserResponse> =
        request
            .flatMap { validator.validate(it) }
            .flatMap { user -> createUserNatsUser.request(user) }


    override fun getAllUsers(request: Mono<GetAllUsersRequest>): Mono<GetAllUsersResponse> =
        request.flatMap { user -> getAllUsersNatsUser.request(user) }

    override fun getUserById(request: Mono<GetUserByIdRequest>): Mono<GetUserByIdResponse> =
        request
            .flatMap { user ->
                getUserByIdNatsUser.request(
                    GetUserByIdRequest.newBuilder().apply { id = user.id }.build()
                )
            }

    override fun updateUser(request: Mono<UpdateUserRequest>): Mono<UpdateUserResponse> =
        request
            .flatMap { validator.validate(it) }
            .flatMap { user -> updateUserNatsUser.request(user) }

}
