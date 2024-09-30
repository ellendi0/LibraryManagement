package com.example.gateway.publisher.author

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdRequest
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAuthorByIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAuthorByIdRequest, GetAuthorByIdResponse> {
    override val subject = NatsSubject.Author.GET_BY_ID
    override val parser: Parser<GetAuthorByIdResponse> = GetAuthorByIdResponse.parser()

    override fun request(request: GetAuthorByIdRequest): Mono<GetAuthorByIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
