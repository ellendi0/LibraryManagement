package com.example.gateway.publisher.bookpresence

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryRequest
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BorrowBookFromLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<BorrowBookFromLibraryRequest, BorrowBookFromLibraryResponse> {
    override val subject = NatsSubject.BookPresence.BORROW_FROM_LIBRARY
    override val parser: Parser<BorrowBookFromLibraryResponse> = BorrowBookFromLibraryResponse.parser()

    override fun request(request: BorrowBookFromLibraryRequest): Mono<BorrowBookFromLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
