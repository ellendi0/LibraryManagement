package com.example.librarymanagement.user.infrastructure.nats

import com.example.internalapi.NatsSubject.User.UPDATE
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserResponse
import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.UpdateUserInPort
import com.example.librarymanagement.user.infrastructure.convertor.UserMapper

@Component
class UpdateUserController(
    private val updateUserInPort: UpdateUserInPort,
    private val userMapper: UserMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdateUserRequest, UpdateUserResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdateUserRequest> = UpdateUserRequest.parser()

    override fun handle(request: UpdateUserRequest): Mono<UpdateUserResponse> {
        return userMapper.toUser(request)
            .let { updateUserInPort.updateUser(it) }
            .map { userMapper.toUserProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(userOutput: UserOutput): UpdateUserResponse {
        return UpdateUserResponse.newBuilder().apply { successBuilder.setUser(userOutput) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): UpdateUserResponse {
        return UpdateUserResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                is DuplicateKeyException -> failureBuilder.setDuplicateKeyError(errorProto)
                else -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
