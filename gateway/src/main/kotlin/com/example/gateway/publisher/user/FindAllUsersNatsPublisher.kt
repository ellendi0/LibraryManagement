package com.example.gateway.publisher.user

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.user.find_all.proto.FindAllUsersRequest
import com.example.internalapi.request.user.find_all.proto.FindAllUsersResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllUsersNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllUsersRequest, FindAllUsersResponse> {
    override val subject = NatsSubject.User.FIND_ALL
    override val parser: Parser<FindAllUsersResponse> = FindAllUsersResponse.parser()

    override fun request(request: FindAllUsersRequest): Mono<FindAllUsersResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
