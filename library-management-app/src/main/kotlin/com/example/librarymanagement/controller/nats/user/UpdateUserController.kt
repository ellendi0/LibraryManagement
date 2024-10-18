package com.example.librarymanagement.controller.nats.user

import com.example.internalapi.NatsSubject.User.UPDATE
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.UserMapper
import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.UserService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateUserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdateUserRequest, UpdateUserResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdateUserRequest> = UpdateUserRequest.parser()

    override fun handle(request: UpdateUserRequest): Mono<UpdateUserResponse> {
        return userMapper.toUser(request)
            .let { userService.updateUser(it) }
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
