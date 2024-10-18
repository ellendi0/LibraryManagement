package com.example.librarymanagement.book.infrastructure.nats

import com.example.internalapi.NatsSubject.Book.GET_BY_TITLE_AND_AUTHOR
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorRequest
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorResponse
import com.example.librarymanagement.book.application.port.`in`.GetBookByTitleAndAuthorInPort
import com.example.librarymanagement.book.infrastructure.convertor.BookMapper
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetBookByTitleAndAuthorNatsController(
    private val getBookByTitleAndAuthorInPort: GetBookByTitleAndAuthorInPort,
    private val bookMapper: BookMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetBookByTitleAndAuthorRequest, GetBookByTitleAndAuthorResponse> {
    override val subject = GET_BY_TITLE_AND_AUTHOR
    override val parser: Parser<GetBookByTitleAndAuthorRequest> = GetBookByTitleAndAuthorRequest.parser()

    override fun handle(request: GetBookByTitleAndAuthorRequest): Mono<GetBookByTitleAndAuthorResponse> {
        return getBookByTitleAndAuthorInPort.getBookByTitleAndAuthor(request.title, request.authorId)
            .map { bookMapper.toBookProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(books: List<Book>): GetBookByTitleAndAuthorResponse {
        return GetBookByTitleAndAuthorResponse.newBuilder().apply { successBuilder.booksBuilder.addAllBooks(books) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): GetBookByTitleAndAuthorResponse {
        return when (exception) {
            is EntityNotFoundException -> {
                GetBookByTitleAndAuthorResponse.newBuilder().apply {
                    failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
                }.build()
            }

            is IllegalArgumentException -> {
                GetBookByTitleAndAuthorResponse.newBuilder().apply {
                    failureBuilder.setIllegalArgumentExpression(errorMapper.toErrorProto(exception))
                }.build()
            }

            else -> {
                GetBookByTitleAndAuthorResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
                }.build()
            }
        }
    }
}
