package com.example.librarymanagement.author.infrastructure.nats

import com.example.internalapi.NatsSubject.Author.CREATE
import com.example.internalapi.model.Author
import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse
import com.example.librarymanagement.author.application.port.`in`.CreateAuthorInPort
import com.example.librarymanagement.author.infrastructure.convertor.AuthorMapper
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateAuthorNatsController(
    private val createAuthorInPort: CreateAuthorInPort,
    private val authorMapper: AuthorMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreateAuthorRequest, CreateAuthorResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateAuthorRequest> = CreateAuthorRequest.parser()

    override fun handle(request: CreateAuthorRequest): Mono<CreateAuthorResponse> {
        return authorMapper.toAuthor(request)
            .let { createAuthorInPort.createAuthor(it) }
            .map { authorMapper.toAuthorProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(author: Author): CreateAuthorResponse {
        return CreateAuthorResponse.newBuilder().apply { successBuilder.setAuthor(author) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): CreateAuthorResponse {
        return CreateAuthorResponse.newBuilder().apply {
            failureBuilder.setUnknownError(errorMapper.toErrorProto(exception))
        }.build()
    }
}
