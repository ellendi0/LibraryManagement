package com.example.librarymanagement.controller.nats.book

import com.example.internalapi.NatsSubject.Book.UPDATE
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookMapper
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.BookService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateBookNatsController(
    private val bookService: BookService,
    private val bookMapper: BookMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdateBookRequest, UpdateBookResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdateBookRequest> = UpdateBookRequest.parser()

    override fun handle(request: UpdateBookRequest): Mono<UpdateBookResponse> {
        return bookMapper.toBook(request)
            .let { bookService.updateBook(it) }
            .map { bookMapper.toBookProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(book: Book): UpdateBookResponse {
        return UpdateBookResponse.newBuilder().apply { successBuilder.setBook(book) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): UpdateBookResponse {
        return UpdateBookResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentError(errorProto)
                else -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
