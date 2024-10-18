package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.delete.proto.DeleteBookRequest
import com.example.internalapi.request.book.delete.proto.DeleteBookResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteBookNatsPublisher(
    private val connection: Connection
) : NatsPublisher<DeleteBookRequest, DeleteBookResponse> {
    override val subject = NatsSubject.Book.UPDATE
    override val parser: Parser<DeleteBookResponse> = DeleteBookResponse.parser()

    override fun request(request: DeleteBookRequest): Mono<DeleteBookResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
