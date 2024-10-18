package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.get_all.proto.GetAllBooksRequest
import com.example.internalapi.request.book.get_all.proto.GetAllBooksResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllBookNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllBooksRequest, GetAllBooksResponse> {
    override val subject = NatsSubject.Book.GET_ALL
    override val parser: Parser<GetAllBooksResponse> = GetAllBooksResponse.parser()

    override fun request(request: GetAllBooksRequest): Mono<GetAllBooksResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
