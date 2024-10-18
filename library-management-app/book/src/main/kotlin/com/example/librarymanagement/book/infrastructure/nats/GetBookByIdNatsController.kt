package com.example.librarymanagement.book.infrastructure.nats

import com.example.internalapi.NatsSubject.Book.GET_BY_ID
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdRequest
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdResponse
import com.example.librarymanagement.book.application.port.`in`.GetByIdBookInPort
import com.example.librarymanagement.book.infrastructure.convertor.BookMapper
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetBookByIdNatsController(
    private val getByIdBookInPort: GetByIdBookInPort,
    private val bookMapper: BookMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetBookByIdRequest, GetBookByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetBookByIdRequest> = GetBookByIdRequest.parser()

    override fun handle(request: GetBookByIdRequest): Mono<GetBookByIdResponse> {
        return getByIdBookInPort.getBookById(request.id)
            .map { bookMapper.toBookProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(book: Book): GetBookByIdResponse {
        return GetBookByIdResponse.newBuilder().apply { successBuilder.setBook(book) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): GetBookByIdResponse {
        return when (exception) {
            is EntityNotFoundException -> {
                GetBookByIdResponse.newBuilder().apply {
                    failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
                }.build()
            }

            is IllegalArgumentException -> {
                GetBookByIdResponse.newBuilder().apply {
                    failureBuilder.setIllegalArgumentExpression(errorMapper.toErrorProto(exception))
                }.build()
            }

            else -> {
                GetBookByIdResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
                }.build()
            }
        }
    }
}
