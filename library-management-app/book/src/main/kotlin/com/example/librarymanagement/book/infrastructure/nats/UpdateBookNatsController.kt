package com.example.librarymanagement.book.infrastructure.nats

import com.example.internalapi.NatsSubject.Book.UPDATE
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookResponse
import com.example.librarymanagement.book.application.port.`in`.UpdateBookInPort
import com.example.librarymanagement.book.infrastructure.convertor.BookMapper
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateBookNatsController(
    private val updateBookInPort: UpdateBookInPort,
    private val bookMapper: BookMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdateBookRequest, UpdateBookResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdateBookRequest> = UpdateBookRequest.parser()

    override fun handle(request: UpdateBookRequest): Mono<UpdateBookResponse> {
        return bookMapper.toBook(request)
            .let { updateBookInPort.updateBook(it) }
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
