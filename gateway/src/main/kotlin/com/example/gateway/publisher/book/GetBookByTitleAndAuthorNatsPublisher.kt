package com.example.gateway.publisher.book

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorRequest
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetBookByTitleAndAuthorNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetBookByTitleAndAuthorRequest, GetBookByTitleAndAuthorResponse> {
    override val subject = NatsSubject.Book.GET_BY_TITLE_AND_AUTHOR
    override val parser: Parser<GetBookByTitleAndAuthorResponse> = GetBookByTitleAndAuthorResponse.parser()

    override fun request(request: GetBookByTitleAndAuthorRequest): Mono<GetBookByTitleAndAuthorResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
