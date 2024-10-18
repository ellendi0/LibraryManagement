package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.find_all_by_library_id_and_book_id.proto.FindAllBooksByLibraryIdAndBookIdRequest
import com.example.internalapi.request.book_presence.find_all_by_library_id_and_book_id.proto.FindAllBooksByLibraryIdAndBookIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllByLibraryIdAndBookIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllBooksByLibraryIdAndBookIdRequest, FindAllBooksByLibraryIdAndBookIdResponse> {
    override val subject = NatsSubject.BookPresence.FIND_ALL_BY_LIBRARY_ID_AND_BOOK_ID
    override val parser: Parser<FindAllBooksByLibraryIdAndBookIdResponse> =
        FindAllBooksByLibraryIdAndBookIdResponse.parser()

    override fun request(
        request: FindAllBooksByLibraryIdAndBookIdRequest
    ): Mono<FindAllBooksByLibraryIdAndBookIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
