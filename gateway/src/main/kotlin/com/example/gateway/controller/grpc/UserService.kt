package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.exception.DuplicateKeyException
import com.example.com.example.gateway.validation.UserRequestValidation
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.user.CreateUserNatsPublisher
import com.example.gateway.publisher.user.GetAllUsersNatsPublisher
import com.example.gateway.publisher.user.GetUserByIdNatsPublisher
import com.example.gateway.publisher.user.UpdateUserNatsPublisher
import com.example.internalapi.ReactorUserServiceGrpc
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.create.proto.CreateUserResponse
import com.example.internalapi.request.user.create.proto.CreateUserResponse.Failure
import com.example.internalapi.request.user.get_all.proto.GetAllUsersRequest
import com.example.internalapi.request.user.get_all.proto.GetAllUsersResponse
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdRequest
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdResponse
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class UserService(
    private val createUserNatsUser: CreateUserNatsPublisher,
    private val getAllUsersNatsUser: GetAllUsersNatsPublisher,
    private val getUserByIdNatsUser: GetUserByIdNatsPublisher,
    private val updateUserNatsUser: UpdateUserNatsPublisher,
    private val validator: UserRequestValidation
) : ReactorUserServiceGrpc.UserServiceImplBase() {

    override fun createUser(request: Mono<CreateUserRequest>): Mono<CreateUserResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { user -> createUserNatsUser.request(user) }
            .flatMap {
                if (it.hasSuccess()) handleCreationSuccess(it) else Mono.error(handleCreationFailure(it.failure))
            }
    }

    override fun getAllUsers(request: Mono<GetAllUsersRequest>): Mono<GetAllUsersResponse> {
        return request
            .flatMap { user -> getAllUsersNatsUser.request(user) }
            .flatMap { handleGetAllUsersSuccess(it) }
    }

    override fun getUserById(request: Mono<GetUserByIdRequest>): Mono<GetUserByIdResponse> {
        return request
            .flatMap { user ->
                getUserByIdNatsUser.request(
                    GetUserByIdRequest.newBuilder().apply { id = user.id }.build()
                )
            }
            .flatMap {
                if (it.hasSuccess()) {
                    handleGetUserByIdSuccess(it)
                } else {
                    Mono.error(handleGetUserByIdFailure(it.failure))
                }
            }
    }

    override fun updateUser(request: Mono<UpdateUserRequest>): Mono<UpdateUserResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { user -> updateUserNatsUser.request(user) }
            .flatMap {
                if (it.hasSuccess()) {
                    Mono.just(handleUpdateUserSuccess(it))
                } else {
                    Mono.error(handleUpdateUserFailure(it.failure))
                }
            }
    }

    private fun handleGetAllUsersSuccess(response: GetAllUsersResponse): Mono<GetAllUsersResponse> {
        return GetAllUsersResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationSuccess(response: CreateUserResponse): Mono<CreateUserResponse> {
        return CreateUserResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            Failure.ErrorCase.DUPLICATE_KEY_ERROR -> DuplicateKeyException(failure.duplicateKeyError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleGetUserByIdSuccess(response: GetUserByIdResponse): Mono<GetUserByIdResponse> {
        return GetUserByIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleGetUserByIdFailure(failure: GetUserByIdResponse.Failure): Exception {
        return when (failure.errorCase) {
            GetUserByIdResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            GetUserByIdResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            GetUserByIdResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            GetUserByIdResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleUpdateUserSuccess(response: UpdateUserResponse): UpdateUserResponse {
        return UpdateUserResponse.newBuilder().setSuccess(response.success).build()
    }

    private fun handleUpdateUserFailure(failure: UpdateUserResponse.Failure): Exception {
        return when (failure.errorCase) {
            UpdateUserResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            UpdateUserResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            UpdateUserResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            UpdateUserResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()

            UpdateUserResponse.Failure.ErrorCase.DUPLICATE_KEY_ERROR ->
                DuplicateKeyException(failure.duplicateKeyError.messages)

            null -> IllegalStateException("Error case is null")

        }
    }
}
