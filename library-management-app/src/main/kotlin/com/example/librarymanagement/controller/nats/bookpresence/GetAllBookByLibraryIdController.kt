package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.GET_ALL_BY_LIBRARY_ID
import com.example.internalapi.model.BookPresence
import com.example.internalapi.request.book_presence.get_all_by_library_id.proto.GetAllBooksByLibraryIdRequest
import com.example.internalapi.request.book_presence.get_all_by_library_id.proto.GetAllBooksByLibraryIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookPresenceMapper
import com.example.librarymanagement.service.BookPresenceService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllBookByLibraryIdController(
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper,
) : NatsController<GetAllBooksByLibraryIdRequest, GetAllBooksByLibraryIdResponse> {
    override val subject = GET_ALL_BY_LIBRARY_ID
    override val parser: Parser<GetAllBooksByLibraryIdRequest> = GetAllBooksByLibraryIdRequest.parser()

    override fun handle(request: GetAllBooksByLibraryIdRequest): Mono<GetAllBooksByLibraryIdResponse> {
        return bookPresenceService.getAllByLibraryId(request.libraryId)
            .map { bookPresenceMapper.toBookPresenceProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(bookPresences: List<BookPresence>): GetAllBooksByLibraryIdResponse {
        return GetAllBooksByLibraryIdResponse.newBuilder()
            .apply { successBuilder.bookPresencesBuilder.addAllBookPresences(bookPresences) }
            .build()
    }
}
