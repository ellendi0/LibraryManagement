package com.example.com.example.gateway.publisher.publisher

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.UpdatePublisherResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdatePublisherNatsPublisher(
    private val connection: Connection
) : NatsPublisher<UpdatePublisherRequest, UpdatePublisherResponse> {
    override val subject = NatsSubject.Publisher.UPDATE
    override val parser: Parser<UpdatePublisherResponse> = UpdatePublisherResponse.parser()

    override fun request(request: UpdatePublisherRequest): Mono<UpdatePublisherResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
