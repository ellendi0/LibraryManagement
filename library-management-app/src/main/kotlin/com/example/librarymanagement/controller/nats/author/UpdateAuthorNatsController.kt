package com.example.librarymanagement.controller.nats.author

import com.example.internalapi.NatsSubject.Author.UPDATE
import com.example.internalapi.model.Author
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.AuthorMapper
import com.example.librarymanagement.dto.mapper.nats.ErrorMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.service.AuthorService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateAuthorNatsController(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper,
    private val errorMapper: ErrorMapper
) : NatsController<UpdateAuthorRequest, UpdateAuthorResponse> {
    override val subject = UPDATE
    override val parser: Parser<UpdateAuthorRequest> = UpdateAuthorRequest.parser()

    override fun handle(request: UpdateAuthorRequest): Mono<UpdateAuthorResponse> {
        return authorMapper.toAuthor(request)
            .let { authorService.updateAuthor(it) }
            .map { authorMapper.toAuthorProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(author: Author): UpdateAuthorResponse {
        return UpdateAuthorResponse.newBuilder().apply { successBuilder.setAuthor(author) }.build()
    }

    private fun buildFailureResponse(exception: Throwable): UpdateAuthorResponse {
        return UpdateAuthorResponse.newBuilder().apply {
            val errorProto = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(errorProto)
                is IllegalArgumentException -> failureBuilder.setIllegalArgumentExpression(errorProto)
                else -> failureBuilder.setUnknownError(errorProto)
            }
        }.build()
    }
}
