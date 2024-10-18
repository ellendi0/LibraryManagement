package com.example.gateway.publisher.user

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.user.update.proto.UpdateUserRequest
import com.example.internalapi.request.user.update.proto.UpdateUserResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateUserNatsPublisher(
    private val connection: Connection
) : NatsPublisher<UpdateUserRequest, UpdateUserResponse> {
    override val subject = NatsSubject.User.UPDATE
    override val parser: Parser<UpdateUserResponse> = UpdateUserResponse.parser()

    override fun request(request: UpdateUserRequest): Mono<UpdateUserResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
