package com.example.gateway.publisher.user

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdRequest
import com.example.internalapi.request.user.get_by_id.proto.GetUserByIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetUserByIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetUserByIdRequest, GetUserByIdResponse> {
    override val subject = NatsSubject.User.GET_BY_ID
    override val parser: Parser<GetUserByIdResponse> = GetUserByIdResponse.parser()

    override fun request(request: GetUserByIdRequest): Mono<GetUserByIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
