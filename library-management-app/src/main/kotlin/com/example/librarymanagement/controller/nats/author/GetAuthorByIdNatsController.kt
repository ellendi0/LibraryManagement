package com.example.librarymanagement.controller.nats.author

import com.example.internalapi.NatsSubject.Author.GET_BY_ID
import com.example.internalapi.model.Author
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdRequest
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.dto.mapper.nats.AuthorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.AuthorService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAuthorByIdNatsController(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper,
    private val errorMapper: ErrorMapper
) : NatsController<GetAuthorByIdRequest, GetAuthorByIdResponse> {
    override val subject = GET_BY_ID
    override val parser: Parser<GetAuthorByIdRequest> = GetAuthorByIdRequest.parser()

    override fun handle(request: GetAuthorByIdRequest): Mono<GetAuthorByIdResponse> {
        return authorService.getAuthorById(request.id)
            .map { authorMapper.toAuthorProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(author: Author): GetAuthorByIdResponse {
        return GetAuthorByIdResponse.newBuilder().apply { successBuilder.setAuthor(author) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): GetAuthorByIdResponse {
        return when (exception) {
            is EntityNotFoundException -> {
                GetAuthorByIdResponse.newBuilder().apply {
                    failureBuilder.setNotFoundError(errorMapper.toErrorProto(exception))
                }.build()
            }

            is IllegalArgumentException -> {
                GetAuthorByIdResponse.newBuilder().apply {
                    failureBuilder.setIllegalArgumentExpression(errorMapper.toErrorProto(exception))
                }.build()
            }

            else -> {
                GetAuthorByIdResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
                }.build()
            }
        }
    }

}
