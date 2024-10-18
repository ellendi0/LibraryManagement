package com.example.gateway.publisher.author

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsRequest
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllAuthorNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllAuthorsRequest, GetAllAuthorsResponse> {
    override val subject = NatsSubject.Author.GET_ALL
    override val parser: Parser<GetAllAuthorsResponse> = GetAllAuthorsResponse.parser()

    override fun request(request: GetAllAuthorsRequest): Mono<GetAllAuthorsResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
