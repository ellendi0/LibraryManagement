package com.example.gateway.publisher.user

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.user.create.proto.CreateUserRequest
import com.example.internalapi.request.user.create.proto.CreateUserResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateUserNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CreateUserRequest, CreateUserResponse> {
    override val subject = NatsSubject.User.CREATE
    override val parser: Parser<CreateUserResponse> = CreateUserResponse.parser()

    override fun request(request: CreateUserRequest): Mono<CreateUserResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
