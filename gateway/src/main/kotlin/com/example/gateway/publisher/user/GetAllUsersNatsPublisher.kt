package com.example.gateway.publisher.user

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.user.get_all.proto.GetAllUsersRequest
import com.example.internalapi.request.user.get_all.proto.GetAllUsersResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllUsersNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllUsersRequest, GetAllUsersResponse> {
    override val subject = NatsSubject.User.GET_ALL
    override val parser: Parser<GetAllUsersResponse> = GetAllUsersResponse.parser()

    override fun request(request: GetAllUsersRequest): Mono<GetAllUsersResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
