package com.example.librarymanagement.controller.nats.book

import com.example.internalapi.NatsSubject.Book.CREATE
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.service.BookService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteBookNatsController(
    private val bookService: BookService,
) : NatsController<CreateBookRequest, CreateBookResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateBookRequest> = CreateBookRequest.parser()

    override fun handle(request: CreateBookRequest): Mono<CreateBookResponse> {
        return bookService.deleteBookById(request.book.id)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): CreateBookResponse {
        return CreateBookResponse.newBuilder().apply { successBuilder }.build()
    }
}
