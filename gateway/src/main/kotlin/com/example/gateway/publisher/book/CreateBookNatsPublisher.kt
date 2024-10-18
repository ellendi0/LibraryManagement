package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateBookNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CreateBookRequest, CreateBookResponse> {
    override val subject = NatsSubject.Book.CREATE
    override val parser: Parser<CreateBookResponse> = CreateBookResponse.parser()

    override fun request(request: CreateBookRequest): Mono<CreateBookResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
