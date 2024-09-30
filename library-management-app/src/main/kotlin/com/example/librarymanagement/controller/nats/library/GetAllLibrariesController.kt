package com.example.librarymanagement.controller.nats.library

import com.example.internalapi.NatsSubject.Library.GET_ALL
import com.example.internalapi.model.Library
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesRequest
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.LibraryMapper
import com.example.librarymanagement.service.LibraryService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllLibrariesController(
    private val libraryService: LibraryService,
    private val libraryMapper: LibraryMapper
) : NatsController<GetAllLibrariesRequest, GetAllLibrariesResponse> {
    override val subject = GET_ALL
    override val parser: Parser<GetAllLibrariesRequest> = GetAllLibrariesRequest.parser()

    override fun handle(request: GetAllLibrariesRequest): Mono<GetAllLibrariesResponse> {
        return libraryService.getAllLibraries()
            .map { libraryMapper.toLibraryProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(libraries: List<Library>): GetAllLibrariesResponse {
        return GetAllLibrariesResponse.newBuilder()
            .apply { successBuilder.librariesBuilder.addAllLibraries(libraries) }
            .build()
    }
}
