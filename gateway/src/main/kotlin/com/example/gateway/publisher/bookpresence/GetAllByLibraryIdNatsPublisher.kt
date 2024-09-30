package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.get_all_by_library_id.proto.GetAllBooksByLibraryIdRequest
import com.example.internalapi.request.book_presence.get_all_by_library_id.proto.GetAllBooksByLibraryIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllByLibraryIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllBooksByLibraryIdRequest, GetAllBooksByLibraryIdResponse> {
    override val subject = NatsSubject.BookPresence.GET_ALL_BY_LIBRARY_ID
    override val parser: Parser<GetAllBooksByLibraryIdResponse> = GetAllBooksByLibraryIdResponse.parser()

    override fun request(request: GetAllBooksByLibraryIdRequest): Mono<GetAllBooksByLibraryIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
