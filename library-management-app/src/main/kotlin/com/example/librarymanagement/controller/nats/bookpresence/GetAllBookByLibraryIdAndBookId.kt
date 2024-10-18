package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.GET_ALL_BY_LIBRARY_ID_AND_BOOK_ID
import com.example.internalapi.model.BookPresence
import com.example.internalapi.request.book_presence.get_all_by_library_id_and_book_id.proto.GetAllBooksByLibraryIdAndBookIdRequest
import com.example.internalapi.request.book_presence.get_all_by_library_id_and_book_id.proto.GetAllBooksByLibraryIdAndBookIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookPresenceMapper
import com.example.librarymanagement.service.BookPresenceService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllBookByLibraryIdAndBookId(
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper,
) : NatsController<GetAllBooksByLibraryIdAndBookIdRequest, GetAllBooksByLibraryIdAndBookIdResponse> {
    override val subject = GET_ALL_BY_LIBRARY_ID_AND_BOOK_ID
    override val parser: Parser<GetAllBooksByLibraryIdAndBookIdRequest> =
        GetAllBooksByLibraryIdAndBookIdRequest.parser()

    override fun handle(request: GetAllBooksByLibraryIdAndBookIdRequest): Mono<GetAllBooksByLibraryIdAndBookIdResponse> {
        return bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(request.libraryId, request.bookId)
            .map { bookPresenceMapper.toBookPresenceProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(bookPresences: List<BookPresence>): GetAllBooksByLibraryIdAndBookIdResponse {
        return GetAllBooksByLibraryIdAndBookIdResponse.newBuilder()
            .apply { successBuilder.bookPresencesBuilder.addAllBookPresences(bookPresences) }
            .build()
    }
}
