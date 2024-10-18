package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.find_all.proto.FindAllBooksRequest
import com.example.internalapi.request.book.find_all.proto.FindAllBooksResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllBookNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllBooksRequest, FindAllBooksResponse> {
    override val subject = NatsSubject.Book.FIND_ALL
    override val parser: Parser<FindAllBooksResponse> = FindAllBooksResponse.parser()

    override fun request(request: FindAllBooksRequest): Mono<FindAllBooksResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
