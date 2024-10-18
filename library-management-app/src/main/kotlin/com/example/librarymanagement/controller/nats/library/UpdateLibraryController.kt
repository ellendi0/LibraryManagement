package com.example.librarymanagement.controller.nats.library

import com.example.internalapi.NatsSubject.Library.UPDATE
import com.example.internalapi.model.Library
import com.example.internalapi.request.author.update.proto.UpdateAuthorResponse
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.LibraryMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.LibraryService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateLibraryController(
    private val libraryService: LibraryService,
    private val libraryMapper: LibraryMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdateLibraryRequest, UpdateLibraryResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdateLibraryRequest> = UpdateLibraryRequest.parser()

    override fun handle(request: UpdateLibraryRequest): Mono<UpdateLibraryResponse> {
        return libraryMapper.toLibrary(request)
            .let { libraryService.updateLibrary(it) }
            .map { libraryMapper.toLibraryProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(library: Library): UpdateLibraryResponse {
        return UpdateLibraryResponse.newBuilder().apply { successBuilder.setLibrary(library) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): UpdateLibraryResponse {
        return UpdateLibraryResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                else -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
