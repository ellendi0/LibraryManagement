package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.get_all_by_library_id_and_book_id.proto.GetAllBooksByLibraryIdAndBookIdRequest
import com.example.internalapi.request.book_presence.get_all_by_library_id_and_book_id.proto.GetAllBooksByLibraryIdAndBookIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllByLibraryIdAndBookIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllBooksByLibraryIdAndBookIdRequest, GetAllBooksByLibraryIdAndBookIdResponse> {
    override val subject = NatsSubject.BookPresence.GET_ALL_BY_LIBRARY_ID_AND_BOOK_ID
    override val parser: Parser<GetAllBooksByLibraryIdAndBookIdResponse> =
        GetAllBooksByLibraryIdAndBookIdResponse.parser()

    override fun request(request: GetAllBooksByLibraryIdAndBookIdRequest): Mono<GetAllBooksByLibraryIdAndBookIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
