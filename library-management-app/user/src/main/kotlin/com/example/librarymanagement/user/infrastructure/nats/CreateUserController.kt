package com.example.librarymanagement.user.infrastructure.nats

import com.example.internalapi.NatsSubject.User.CREATE
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.create.proto.CreateUserResponse
import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.CreateUserInPort
import com.example.librarymanagement.user.infrastructure.convertor.UserMapper

@Component
class CreateUserController(
    private val createUserInPort: CreateUserInPort,
    private val userMapper: UserMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreateUserRequest, CreateUserResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateUserRequest> = CreateUserRequest.parser()

    override fun handle(request: CreateUserRequest): Mono<CreateUserResponse> {
        return userMapper.toUser(request)
            .let { createUserInPort.createUser(it) }
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
