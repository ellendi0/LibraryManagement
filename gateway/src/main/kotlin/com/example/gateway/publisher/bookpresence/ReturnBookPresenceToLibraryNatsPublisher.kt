package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryRequest
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReturnBookPresenceToLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<ReturnBookToLibraryRequest, ReturnBookToLibraryResponse> {
    override val subject = NatsSubject.BookPresence.RETURN_TO_LIBRARY
    override val parser: Parser<ReturnBookToLibraryResponse> = ReturnBookToLibraryResponse.parser()

    override fun request(request: ReturnBookToLibraryRequest): Mono<ReturnBookToLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
