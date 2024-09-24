package com.example.gateway.publisher.publisher

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreatePublisherNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CreatePublisherRequest, CreatePublisherResponse> {
    override val subject = NatsSubject.Publisher.CREATE
    override val parser: Parser<CreatePublisherResponse> = CreatePublisherResponse.parser()

    override fun request(request: CreatePublisherRequest): Mono<CreatePublisherResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
