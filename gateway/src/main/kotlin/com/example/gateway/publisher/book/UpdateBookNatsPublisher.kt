package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateBookNatsPublisher(
    private val connection: Connection
) : NatsPublisher<UpdateBookRequest, UpdateBookResponse> {
    override val subject = NatsSubject.Book.UPDATE
    override val parser: Parser<UpdateBookResponse> = UpdateBookResponse.parser()

    override fun request(request: UpdateBookRequest): Mono<UpdateBookResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
