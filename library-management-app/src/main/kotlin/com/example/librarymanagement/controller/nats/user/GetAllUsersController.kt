package com.example.librarymanagement.controller.nats.user

import com.example.internalapi.NatsSubject.User.GET_ALL
import com.example.internalapi.model.UserOutput
import com.example.internalapi.request.user.get_all.proto.GetAllUsersRequest
import com.example.internalapi.request.user.get_all.proto.GetAllUsersResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.UserMapper
import com.example.librarymanagement.service.UserService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllUsersController(
    private val userService: UserService,
    private val userMapper: UserMapper
) : NatsController<GetAllUsersRequest, GetAllUsersResponse> {
    override val subject = GET_ALL
    override val parser: Parser<GetAllUsersRequest> = GetAllUsersRequest.parser()

    override fun handle(request: GetAllUsersRequest): Mono<GetAllUsersResponse> {
        return userService.getAllUsers()
            .map { userMapper.toUserProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(userOutput: List<UserOutput>): GetAllUsersResponse {
        return GetAllUsersResponse.newBuilder()
            .apply { successBuilder.usersBuilder.addAllUsers(userOutput) }
            .build()
    }
}
