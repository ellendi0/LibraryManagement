package com.example.gateway.publisher.author

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.author.find_all.proto.FindAllAuthorsRequest
import com.example.internalapi.request.author.find_all.proto.FindAllAuthorsResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllAuthorNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllAuthorsRequest, FindAllAuthorsResponse> {
    override val subject = NatsSubject.Author.FIND_ALL
    override val parser: Parser<FindAllAuthorsResponse> = FindAllAuthorsResponse.parser()

    override fun request(request: FindAllAuthorsRequest): Mono<FindAllAuthorsResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
