package com.example.librarymanagement.library.infrastructure.nats

import com.example.internalapi.NatsSubject.Library.CREATE
import com.example.internalapi.model.Library
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.library.application.port.`in`.CreateLibraryInPort
import com.example.librarymanagement.library.infrastructure.convertor.LibraryMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateLibraryController(
    private val createLibraryInPort: CreateLibraryInPort,
    private val libraryMapper: LibraryMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreateLibraryRequest, CreateLibraryResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateLibraryRequest> = CreateLibraryRequest.parser()

    override fun handle(request: CreateLibraryRequest): Mono<CreateLibraryResponse> {
        return libraryMapper.toLibrary(request)
            .let { createLibraryInPort.createLibrary(it) }
            .map { libraryMapper.toLibraryProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(library: Library): CreateLibraryResponse {
        return CreateLibraryResponse.newBuilder().apply { successBuilder.setLibrary(library) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): CreateLibraryResponse {
        return CreateLibraryResponse.newBuilder().apply {
            failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
        }.build()
    }
}
