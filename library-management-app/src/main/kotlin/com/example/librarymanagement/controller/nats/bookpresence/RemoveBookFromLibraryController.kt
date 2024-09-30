package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.REMOVE_FROM_LIBRARY
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryRequest
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.service.BookPresenceService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RemoveBookFromLibraryController(
    private val bookPresenceService: BookPresenceService
) : NatsController<RemoveBookFromLibraryRequest, RemoveBookFromLibraryResponse> {
    override val subject = REMOVE_FROM_LIBRARY
    override val parser: Parser<RemoveBookFromLibraryRequest> = RemoveBookFromLibraryRequest.parser()

    override fun handle(request: RemoveBookFromLibraryRequest): Mono<RemoveBookFromLibraryResponse> {
        return bookPresenceService.deleteBookPresenceById(request.bookPresenceId)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): RemoveBookFromLibraryResponse {
        return RemoveBookFromLibraryResponse.newBuilder().apply { successBuilder }.build()
    }
}
