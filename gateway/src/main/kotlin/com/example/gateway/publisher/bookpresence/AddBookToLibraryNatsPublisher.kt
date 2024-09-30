package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryRequest
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AddBookToLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<AddBookToLibraryRequest, AddBookToLibraryResponse> {
    override val subject = NatsSubject.BookPresence.ADD_TO_LIBRARY
    override val parser: Parser<AddBookToLibraryResponse> = AddBookToLibraryResponse.parser()

    override fun request(request: AddBookToLibraryRequest): Mono<AddBookToLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
