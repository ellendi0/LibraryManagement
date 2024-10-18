package com.example.librarymanagement.user.infrastructure.nats

import com.example.internalapi.NatsSubject.User.FIND_ALL
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.find_all.proto.FindAllUsersRequest
import com.example.internalapi.request.user.find_all.proto.FindAllUsersResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.user.application.port.`in`.FindAllUsersInPort
import com.example.librarymanagement.user.infrastructure.convertor.UserMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllUsersController(
    private val findAllUsersInPort: FindAllUsersInPort,
    private val userMapper: UserMapper
) : NatsController<FindAllUsersRequest, FindAllUsersResponse> {
    override val subject = FIND_ALL
    override val parser: Parser<FindAllUsersRequest> = FindAllUsersRequest.parser()

    override fun handle(request: FindAllUsersRequest): Mono<FindAllUsersResponse> {
        return findAllUsersInPort.findAllUsers()
            .map { userMapper.toUserProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(userOutput: List<UserOutput>): FindAllUsersResponse {
        return FindAllUsersResponse.newBuilder()
            .apply { successBuilder.usersBuilder.addAllUsers(userOutput) }
            .build()
    }
}
