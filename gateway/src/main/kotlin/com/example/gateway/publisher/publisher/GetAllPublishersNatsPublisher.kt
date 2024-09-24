package com.example.com.example.gateway.publisher.publisher

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.publisher.create.proto.GetAllPublishersRequest
import com.example.internalapi.request.publisher.create.proto.GetAllPublishersResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllPublishersNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllPublishersRequest, GetAllPublishersResponse> {
    override val subject = NatsSubject.Publisher.GET_ALL
    override val parser: Parser<GetAllPublishersResponse> = GetAllPublishersResponse.parser()

    override fun request(request: GetAllPublishersRequest): Mono<GetAllPublishersResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
