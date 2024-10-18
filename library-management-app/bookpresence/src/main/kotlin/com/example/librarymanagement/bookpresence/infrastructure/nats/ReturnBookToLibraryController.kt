package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.RETURN_TO_LIBRARY
import com.example.internalapi.model.Journal
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryRequest
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryResponse
import com.example.librarymanagement.bookpresence.application.port.`in`.ReturnBookPresenceToLibraryInPort
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.journal.infrastructure.convertor.JournalMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReturnBookToLibraryController(
    private val returnBookPresenceToLibraryInPort: ReturnBookPresenceToLibraryInPort,
    private val journalMapper: JournalMapper,
    private val errorMapper: ErrorMapper
) : NatsController<ReturnBookToLibraryRequest, ReturnBookToLibraryResponse> {
    override val subject = RETURN_TO_LIBRARY
    override val parser: Parser<ReturnBookToLibraryRequest> = ReturnBookToLibraryRequest.parser()

    override fun handle(request: ReturnBookToLibraryRequest): Mono<ReturnBookToLibraryResponse> {
        return returnBookPresenceToLibraryInPort.returnBookToLibrary(request.userId, request.libraryId, request.bookId)
            .map { journalMapper.toJournalProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(journals: List<Journal>): ReturnBookToLibraryResponse {
        return ReturnBookToLibraryResponse.newBuilder()
            .apply { successBuilder.journalsBuilder.addAllJournals(journals) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): ReturnBookToLibraryResponse {
        return ReturnBookToLibraryResponse.newBuilder().apply {
            failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
        }.build()
    }
}
