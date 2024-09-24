package com.example.com.example.gateway.publisher.publisher

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.create.proto.GetPublisherByIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetPublisherByIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetPublisherByIdRequest, GetPublisherByIdResponse> {
    override val subject = NatsSubject.Publisher.GET_BY_ID
    override val parser: Parser<GetPublisherByIdResponse> = GetPublisherByIdResponse.parser()

    override fun request(request: GetPublisherByIdRequest): Mono<GetPublisherByIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
