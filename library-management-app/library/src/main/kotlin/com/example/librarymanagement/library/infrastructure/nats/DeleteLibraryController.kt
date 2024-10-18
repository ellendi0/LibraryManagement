package com.example.librarymanagement.library.infrastructure.nats

import com.example.internalapi.NatsSubject.Library.CREATE
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.library.application.port.`in`.DeleteLibraryByIdInPort
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteLibraryController(
    private val deleteLibraryByIdInPort: DeleteLibraryByIdInPort,
) : NatsController<CreateLibraryRequest, CreateLibraryResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateLibraryRequest> = CreateLibraryRequest.parser()

    override fun handle(request: CreateLibraryRequest): Mono<CreateLibraryResponse> {
        return deleteLibraryByIdInPort.deleteLibraryById(request.library.id).map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): CreateLibraryResponse {
        return CreateLibraryResponse.newBuilder().apply { successBuilder }.build()
    }
}
