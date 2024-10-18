package com.example.librarymanagement.library.infrastructure.nats

import com.example.internalapi.NatsSubject.Library.FIND_ALL
import com.example.internalapi.model.Library
import com.example.internalapi.request.library.find_all.proto.FindAllLibrariesRequest
import com.example.internalapi.request.library.find_all.proto.FindAllLibrariesResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.library.application.port.`in`.FindAllLibrariesInPort
import com.example.librarymanagement.library.infrastructure.convertor.LibraryMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllLibrariesController(
    private val findAllLibrariesInPort: FindAllLibrariesInPort,
    private val libraryMapper: LibraryMapper
) : NatsController<FindAllLibrariesRequest, FindAllLibrariesResponse> {
    override val subject = FIND_ALL
    override val parser: Parser<FindAllLibrariesRequest> = FindAllLibrariesRequest.parser()

    override fun handle(request: FindAllLibrariesRequest): Mono<FindAllLibrariesResponse> {
        return findAllLibrariesInPort.findAllLibraries()
            .map { libraryMapper.toLibraryProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(libraries: List<Library>): FindAllLibrariesResponse {
        return FindAllLibrariesResponse.newBuilder()
            .apply { successBuilder.librariesBuilder.addAllLibraries(libraries) }
            .build()
    }
}
