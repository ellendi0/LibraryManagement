package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.find_all_by_library_id.proto.FindAllBooksByLibraryIdRequest
import com.example.internalapi.request.book_presence.find_all_by_library_id.proto.FindAllBooksByLibraryIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllByLibraryIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllBooksByLibraryIdRequest, FindAllBooksByLibraryIdResponse> {
    override val subject = NatsSubject.BookPresence.FIND_ALL_BY_LIBRARY_ID
    override val parser: Parser<FindAllBooksByLibraryIdResponse> = FindAllBooksByLibraryIdResponse.parser()

    override fun request(request: FindAllBooksByLibraryIdRequest): Mono<FindAllBooksByLibraryIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
