package com.example.librarymanagement.controller.nats.user

import com.example.internalapi.NatsSubject.User.GET_BY_ID
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdRequest
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.UserMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.UserService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetUserByIdController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetUserByIdRequest, GetUserByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetUserByIdRequest> = GetUserByIdRequest.parser()

    override fun handle(request: GetUserByIdRequest): Mono<GetUserByIdResponse> {
        return userService.getUserById(request.id)
            .map { userMapper.toUserProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(userOutput: UserOutput): GetUserByIdResponse {
        return GetUserByIdResponse.newBuilder().apply { successBuilder.setUser(userOutput) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): GetUserByIdResponse {
        return GetUserByIdResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                else -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
