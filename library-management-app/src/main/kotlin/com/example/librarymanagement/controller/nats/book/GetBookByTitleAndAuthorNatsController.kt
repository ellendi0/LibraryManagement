package com.example.librarymanagement.controller.nats.book

import com.example.internalapi.NatsSubject.Book.GET_BY_TITLE_AND_AUTHOR
import com.example.internalapi.model.Book
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorRequest
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookMapper
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.BookService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetBookByTitleAndAuthorNatsController(
    private val bookService: BookService,
    private val bookMapper: BookMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetBookByTitleAndAuthorRequest, GetBookByTitleAndAuthorResponse> {
    override val subject = GET_BY_TITLE_AND_AUTHOR
    override val parser: Parser<GetBookByTitleAndAuthorRequest> = GetBookByTitleAndAuthorRequest.parser()

    override fun handle(request: GetBookByTitleAndAuthorRequest): Mono<GetBookByTitleAndAuthorResponse> {
        return bookService.getBookByTitleAndAuthor(request.title, request.authorId)
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
