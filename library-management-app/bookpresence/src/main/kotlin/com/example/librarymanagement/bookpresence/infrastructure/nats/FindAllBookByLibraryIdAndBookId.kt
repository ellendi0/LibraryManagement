package com.example.librarymanagement.bookpresence.infrastructure.nats

import com.example.internalapi.NatsSubject.BookPresence.FIND_ALL_BY_LIBRARY_ID_AND_BOOK_ID
import com.example.internalapi.model.BookPresence
import com.example.internalapi.request.book_presence.find_all_by_library_id_and_book_id.proto.FindAllBooksByLibraryIdAndBookIdRequest
import com.example.internalapi.request.book_presence.find_all_by_library_id_and_book_id.proto.FindAllBooksByLibraryIdAndBookIdResponse
import com.example.librarymanagement.bookpresence.application.port.`in`.FindAllBookPresenceByLibraryIdAndBookIdInPort
import com.example.librarymanagement.bookpresence.infrastructure.convertor.BookPresenceMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllBookByLibraryIdAndBookId(
    private val findAllBookPresenceByLibraryIdAndBookIdInPort: FindAllBookPresenceByLibraryIdAndBookIdInPort,
    private val bookPresenceMapper: BookPresenceMapper,
) : NatsController<FindAllBooksByLibraryIdAndBookIdRequest, FindAllBooksByLibraryIdAndBookIdResponse> {
    override val subject = FIND_ALL_BY_LIBRARY_ID_AND_BOOK_ID
    override val parser: Parser<FindAllBooksByLibraryIdAndBookIdRequest> =
        FindAllBooksByLibraryIdAndBookIdRequest.parser()

    override fun handle(request: FindAllBooksByLibraryIdAndBookIdRequest): Mono<FindAllBooksByLibraryIdAndBookIdResponse> {
        return findAllBookPresenceByLibraryIdAndBookIdInPort.findAllBookPresencesByLibraryIdAndBookId(
            request.libraryId,
            request.bookId
        )
            .map { bookPresenceMapper.toBookPresenceProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(bookPresences: List<BookPresence>): FindAllBooksByLibraryIdAndBookIdResponse {
        return FindAllBooksByLibraryIdAndBookIdResponse.newBuilder()
            .apply { successBuilder.bookPresencesBuilder.addAllBookPresences(bookPresences) }
            .build()
    }
}
