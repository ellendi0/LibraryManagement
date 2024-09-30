package com.example.librarymanagement.controller.nats.bookpresence

import com.example.internalapi.NatsSubject.BookPresence.ADD_TO_LIBRARY
import com.example.internalapi.model.BookPresence
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryRequest
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.BookPresenceService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AddBookToLibraryController(
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper,
    private val errorMapper: ErrorMapper,
) : NatsController<AddBookToLibraryRequest, AddBookToLibraryResponse> {
    override val subject = ADD_TO_LIBRARY
    override val parser: Parser<AddBookToLibraryRequest> = AddBookToLibraryRequest.parser()

    override fun handle(request: AddBookToLibraryRequest): Mono<AddBookToLibraryResponse> {
        return bookPresenceService.addBookToLibrary(request.libraryId, request.bookId)
            .map { bookPresenceMapper.toBookPresenceProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(bookPresence: BookPresence): AddBookToLibraryResponse {
        return AddBookToLibraryResponse.newBuilder().apply { successBuilder.setBookPresence(bookPresence) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): AddBookToLibraryResponse {
        return AddBookToLibraryResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                is Exception -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
