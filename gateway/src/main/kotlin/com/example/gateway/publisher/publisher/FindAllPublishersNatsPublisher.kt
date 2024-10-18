package com.example.gateway.publisher.publisher

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.publisher.find_all.proto.FindAllPublishersRequest
import com.example.internalapi.request.publisher.find_all.proto.FindAllPublishersResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllPublishersNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllPublishersRequest, FindAllPublishersResponse> {
    override val subject = NatsSubject.Publisher.FIND_ALL
    override val parser: Parser<FindAllPublishersResponse> = FindAllPublishersResponse.parser()

    override fun request(request: FindAllPublishersRequest): Mono<FindAllPublishersResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
