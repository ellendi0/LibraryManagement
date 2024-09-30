package com.example.librarymanagement.controller.nats.user

import com.example.internalapi.NatsSubject.User.CREATE
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.create.proto.CreateUserResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.UserMapper
import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.service.UserService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateUserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreateUserRequest, CreateUserResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateUserRequest> = CreateUserRequest.parser()

    override fun handle(request: CreateUserRequest): Mono<CreateUserResponse> {
        return userMapper.toUser(request)
            .let { userService.createUser(it) }
            .map { userMapper.toUserProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(userOutput: UserOutput): CreateUserResponse {
        return CreateUserResponse.newBuilder().apply { successBuilder.setUser(userOutput) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): CreateUserResponse {
        return CreateUserResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is DuplicateKeyException -> failureBuilder.setDuplicateKeyError(errorProto)
                is Exception -> failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
            }
        }.build()
    }
}
