package com.example.librarymanagement.library.infrastructure.nats

import com.example.internalapi.NatsSubject.Library.GET_BY_ID
import com.example.internalapi.model.Library
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdRequest
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdResponse
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.library.application.port.`in`.GetLibraryByIdInPort
import com.example.librarymanagement.library.infrastructure.convertor.LibraryMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetLibraryByIdController(
    private val getLibraryByIdInPort: GetLibraryByIdInPort,
    private val libraryMapper: LibraryMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetLibraryByIdRequest, GetLibraryByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetLibraryByIdRequest> = GetLibraryByIdRequest.parser()

    override fun handle(request: GetLibraryByIdRequest): Mono<GetLibraryByIdResponse> {
        return getLibraryByIdInPort.getLibraryById(request.id)
            .map { libraryMapper.toLibraryProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(library: Library): GetLibraryByIdResponse {
        return GetLibraryByIdResponse.newBuilder().apply { successBuilder.setLibrary(library) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): GetLibraryByIdResponse {
        return when (exception) {
            is EntityNotFoundException -> {
                GetLibraryByIdResponse.newBuilder().apply {
                    failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
                }.build()
            }

            is IllegalArgumentException -> {
                GetLibraryByIdResponse.newBuilder().apply {
                    failureBuilder.setIllegalArgumentExpression(errorMapper.toErrorProto(exception))
                }.build()
            }

            else -> {
                GetLibraryByIdResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
                }.build()
            }
        }
    }
}
