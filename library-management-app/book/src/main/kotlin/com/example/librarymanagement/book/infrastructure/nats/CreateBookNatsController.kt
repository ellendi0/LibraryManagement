package com.example.librarymanagement.book.infrastructure.nats

import com.example.internalapi.NatsSubject.Book.CREATE
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import com.example.librarymanagement.book.application.port.`in`.CreateBookInPort
import com.example.librarymanagement.book.infrastructure.convertor.BookMapper
import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateBookNatsController(
    private val createBookInPort: CreateBookInPort,
    private val bookMapper: BookMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreateBookRequest, CreateBookResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateBookRequest> = CreateBookRequest.parser()

    override fun handle(request: CreateBookRequest): Mono<CreateBookResponse> {
        return bookMapper.toBook(request)
            .let { createBookInPort.createBook(it) }
            .map { bookMapper.toBookProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(book: Book): CreateBookResponse {
        return CreateBookResponse.newBuilder().apply { successBuilder.setBook(book) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): CreateBookResponse {
        return CreateBookResponse.newBuilder().apply {
            val error = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(error)
                is DuplicateKeyException -> failureBuilder.setDuplicateKeyError(error)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentError(error)
                is Exception -> failureBuilder.setUnknownError(error)
            }
        }.build()
    }
}
