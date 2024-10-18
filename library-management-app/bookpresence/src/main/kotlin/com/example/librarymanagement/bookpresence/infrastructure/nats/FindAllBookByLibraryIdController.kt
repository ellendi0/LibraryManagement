package com.example.librarymanagement.bookpresence.infrastructure.nats

import com.example.internalapi.NatsSubject.BookPresence.FIND_ALL_BY_LIBRARY_ID
import com.example.internalapi.model.BookPresence
import com.example.internalapi.request.book_presence.find_all_by_library_id.proto.FindAllBooksByLibraryIdRequest
import com.example.internalapi.request.book_presence.find_all_by_library_id.proto.FindAllBooksByLibraryIdResponse
import com.example.librarymanagement.bookpresence.application.port.`in`.FindAllBookPresenceByLibraryIdInPort
import com.example.librarymanagement.bookpresence.infrastructure.convertor.BookPresenceMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllBookByLibraryIdController(
    private val findAllBookPresenceByLibraryIdInPort: FindAllBookPresenceByLibraryIdInPort,
    private val bookPresenceMapper: BookPresenceMapper,
) : NatsController<FindAllBooksByLibraryIdRequest, FindAllBooksByLibraryIdResponse> {
    override val subject = FIND_ALL_BY_LIBRARY_ID
    override val parser: Parser<FindAllBooksByLibraryIdRequest> = FindAllBooksByLibraryIdRequest.parser()

    override fun handle(request: FindAllBooksByLibraryIdRequest): Mono<FindAllBooksByLibraryIdResponse> {
        return findAllBookPresenceByLibraryIdInPort.findAllByLibraryId(request.libraryId)
            .map { bookPresenceMapper.toBookPresenceProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(bookPresences: List<BookPresence>): FindAllBooksByLibraryIdResponse {
        return FindAllBooksByLibraryIdResponse.newBuilder()
            .apply { successBuilder.bookPresencesBuilder.addAllBookPresences(bookPresences) }
            .build()
    }
}
