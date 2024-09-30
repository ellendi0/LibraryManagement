package com.example.librarymanagement.controller.nats.library

import com.example.internalapi.NatsSubject.Library.CREATE
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.service.LibraryService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteLibraryController(
    private val libraryService: LibraryService,
) : NatsController<CreateLibraryRequest, CreateLibraryResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateLibraryRequest> = CreateLibraryRequest.parser()

    override fun handle(request: CreateLibraryRequest): Mono<CreateLibraryResponse> {
        return libraryService.deleteLibraryById(request.library.id)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): CreateLibraryResponse {
        return CreateLibraryResponse.newBuilder().apply { successBuilder }.build()
    }
}
