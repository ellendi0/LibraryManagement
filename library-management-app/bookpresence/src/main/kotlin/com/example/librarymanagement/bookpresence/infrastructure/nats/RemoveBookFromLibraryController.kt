package com.example.librarymanagement.bookpresence.infrastructure.nats

import com.example.internalapi.NatsSubject.BookPresence.REMOVE_FROM_LIBRARY
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryRequest
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryResponse
import com.example.librarymanagement.bookpresence.application.port.`in`.RemoveBookPresenceFromLibraryInPort
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RemoveBookFromLibraryController(
    private val removeBookPresenceFromLibraryInPort: RemoveBookPresenceFromLibraryInPort,
) : NatsController<RemoveBookFromLibraryRequest, RemoveBookFromLibraryResponse> {
    override val subject = REMOVE_FROM_LIBRARY
    override val parser: Parser<RemoveBookFromLibraryRequest> = RemoveBookFromLibraryRequest.parser()

    override fun handle(request: RemoveBookFromLibraryRequest): Mono<RemoveBookFromLibraryResponse> {
        return removeBookPresenceFromLibraryInPort.deleteBookPresenceById(request.bookPresenceId)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): RemoveBookFromLibraryResponse {
        return RemoveBookFromLibraryResponse.newBuilder().apply { successBuilder }.build()
    }
}
