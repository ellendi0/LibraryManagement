package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.RETURN_TO_LIBRARY
import com.example.internalapi.model.Journal
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryRequest
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.JournalMapper
import com.example.librarymanagement.service.BookPresenceService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReturnBookToLibraryController(
    private val bookPresenceService: BookPresenceService,
    private val journalMapper: JournalMapper
) : NatsController<ReturnBookToLibraryRequest, ReturnBookToLibraryResponse> {
    override val subject = RETURN_TO_LIBRARY
    override val parser: Parser<ReturnBookToLibraryRequest> = ReturnBookToLibraryRequest.parser()

    override fun handle(request: ReturnBookToLibraryRequest): Mono<ReturnBookToLibraryResponse> {
        return bookPresenceService.returnBookToLibrary(request.userId, request.libraryId, request.bookId)
            .map { journalMapper.toJournalProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(journals: List<Journal>): ReturnBookToLibraryResponse {
        return ReturnBookToLibraryResponse.newBuilder()
            .apply { successBuilder.journalsBuilder.addAllJournals(journals) }.build()
    }
}
