package com.example.librarymanagement.user.infrastructure.nats

import com.example.internalapi.NatsSubject.User.GET_BY_ID
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdRequest
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdResponse
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.GetUserByIdInPort
import com.example.librarymanagement.user.infrastructure.convertor.UserMapper

@Component
class GetUserByIdController(
    private val getUserByIdInPort: GetUserByIdInPort,
    private val userMapper: UserMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetUserByIdRequest, GetUserByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetUserByIdRequest> = GetUserByIdRequest.parser()

    override fun handle(request: GetUserByIdRequest): Mono<GetUserByIdResponse> {
        return getUserByIdInPort.getUserById(request.id)
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
