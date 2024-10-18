package com.example.librarymanagement.book.infrastructure.nats

import com.example.internalapi.NatsSubject.Book.CREATE
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import com.example.librarymanagement.book.application.port.`in`.DeleteBookByIdInPort
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteBookNatsController(
    private val deleteBookByIdInPort: DeleteBookByIdInPort,
) : NatsController<CreateBookRequest, CreateBookResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateBookRequest> = CreateBookRequest.parser()

    override fun handle(request: CreateBookRequest): Mono<CreateBookResponse> {
        return deleteBookByIdInPort.deleteBookById(request.book.id)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): CreateBookResponse {
        return CreateBookResponse.newBuilder().apply { successBuilder }.build()
    }
}
