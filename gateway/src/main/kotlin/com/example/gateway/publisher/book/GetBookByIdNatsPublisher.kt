package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdRequest
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetBookByIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetBookByIdRequest, GetBookByIdResponse> {
    override val subject = NatsSubject.Book.GET_BY_ID
    override val parser: Parser<GetBookByIdResponse> = GetBookByIdResponse.parser()

    override fun request(request: GetBookByIdRequest): Mono<GetBookByIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
