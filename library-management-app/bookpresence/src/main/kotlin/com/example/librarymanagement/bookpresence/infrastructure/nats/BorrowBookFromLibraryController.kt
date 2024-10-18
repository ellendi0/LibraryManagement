package com.example.librarymanagement.bookpresence.infrastructure.nats

import com.example.internalapi.NatsSubject.BookPresence.BORROW_FROM_LIBRARY
import com.example.internalapi.model.Journal
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryRequest
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryResponse
import com.example.librarymanagement.bookpresence.application.port.`in`.BorrowBookPresenceFromLibraryInPort
import com.example.librarymanagement.core.application.exception.BookAvailabilityException
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.journal.infrastructure.convertor.JournalMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BorrowBookFromLibraryController(
    private val borrowBookPresenceFromLibraryInPort: BorrowBookPresenceFromLibraryInPort,
    private val journalMapper: JournalMapper,
    private val errorMapper: ErrorMapper,
) : NatsController<BorrowBookFromLibraryRequest, BorrowBookFromLibraryResponse> {
    override val subject = BORROW_FROM_LIBRARY
    override val parser: Parser<BorrowBookFromLibraryRequest> = BorrowBookFromLibraryRequest.parser()

    override fun handle(request: BorrowBookFromLibraryRequest): Mono<BorrowBookFromLibraryResponse> {
        return borrowBookPresenceFromLibraryInPort.borrowBookFromLibrary(
            request.userId,
            request.libraryId,
            request.bookId
        )
            .map { journalMapper.toJournalProto(it) }
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
