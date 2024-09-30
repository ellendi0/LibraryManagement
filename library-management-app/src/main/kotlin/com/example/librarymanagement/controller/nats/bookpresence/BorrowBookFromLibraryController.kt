package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.BORROW_FROM_LIBRARY
import com.example.internalapi.model.Journal
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryRequest
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.JournalMapper
import com.example.librarymanagement.exception.BookAvailabilityException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.BookPresenceService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BorrowBookFromLibraryController(
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper,
    private val journalMapper: JournalMapper,
    private val errorMapper: ErrorMapper,
) : NatsController<BorrowBookFromLibraryRequest, BorrowBookFromLibraryResponse> {
    override val subject = BORROW_FROM_LIBRARY
    override val parser: Parser<BorrowBookFromLibraryRequest> = BorrowBookFromLibraryRequest.parser()

    override fun handle(request: BorrowBookFromLibraryRequest): Mono<BorrowBookFromLibraryResponse> {
        return bookPresenceService.borrowBookFromLibrary(request.userId, request.libraryId, request.bookId)
            .map { journalMapper.toJournalProto(it)}
            .collectList()
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(journals: List<Journal>): BorrowBookFromLibraryResponse {
        return BorrowBookFromLibraryResponse.newBuilder()
            .apply { successBuilder.journalsBuilder.addAllJournals(journals) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): BorrowBookFromLibraryResponse {
        return BorrowBookFromLibraryResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)

            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                is BookAvailabilityException -> failureBuilder.setBookAvailabilityError(errorProto)
                is Exception -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
