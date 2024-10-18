package com.example.librarymanagement.book.infrastructure.nats

import com.example.internalapi.NatsSubject.Book.FIND_ALL
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.find_all.proto.FindAllBooksRequest
import com.example.internalapi.request.book.find_all.proto.FindAllBooksResponse
import com.example.librarymanagement.book.application.port.`in`.FindAllBooksInPort
import com.example.librarymanagement.book.infrastructure.convertor.BookMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllBooksNatsController(
    private val findAllBooksInPort: FindAllBooksInPort,
    private val bookMapper: BookMapper
) : NatsController<FindAllBooksRequest, FindAllBooksResponse> {
    override val subject = FIND_ALL
    override val parser: Parser<FindAllBooksRequest> = FindAllBooksRequest.parser()

    override fun handle(request: FindAllBooksRequest): Mono<FindAllBooksResponse> {
        return findAllBooksInPort.findAll()
            .map { bookMapper.toBookProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(book: List<Book>): FindAllBooksResponse {
        return FindAllBooksResponse.newBuilder()
            .apply { successBuilder.booksBuilder.addAllBooks(book) }
            .build()
    }
}
