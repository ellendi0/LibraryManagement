package com.example.librarymanagement.controller.nats.book

import com.example.internalapi.NatsSubject.Book.GET_ALL
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.get_all.proto.GetAllBooksRequest
import com.example.internalapi.request.book.get_all.proto.GetAllBooksResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookMapper
import com.example.librarymanagement.service.BookService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
    class GetAllBooksNatsController(
    private val bookService: BookService,
    private val bookMapper: BookMapper
) : NatsController<GetAllBooksRequest, GetAllBooksResponse> {
    override val subject = GET_ALL
    override val parser: Parser<GetAllBooksRequest> = GetAllBooksRequest.parser()

    override fun handle(request: GetAllBooksRequest): Mono<GetAllBooksResponse> {
        return bookService.getAll()
            .map { bookMapper.toBookProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(book: List<Book>): GetAllBooksResponse {
        return GetAllBooksResponse.newBuilder()
            .apply { successBuilder.booksBuilder.addAllBooks(book) }
            .build()
    }
}
