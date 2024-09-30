package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryRequest
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RemoveBookPresenceFromLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<RemoveBookFromLibraryRequest, RemoveBookFromLibraryResponse> {
    override val subject = NatsSubject.BookPresence.REMOVE_FROM_LIBRARY
    override val parser: Parser<RemoveBookFromLibraryResponse> = RemoveBookFromLibraryResponse.parser()

    override fun request(request: RemoveBookFromLibraryRequest): Mono<RemoveBookFromLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
