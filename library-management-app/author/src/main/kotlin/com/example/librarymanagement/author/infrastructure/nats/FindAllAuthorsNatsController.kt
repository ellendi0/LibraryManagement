package com.example.librarymanagement.author.infrastructure.nats

import com.example.internalapi.NatsSubject.Author.FIND_ALL
import com.example.internalapi.model.Author
import com.example.internalapi.request.author.find_all.proto.FindAllAuthorsRequest
import com.example.internalapi.request.author.find_all.proto.FindAllAuthorsResponse
import com.example.librarymanagement.author.application.port.`in`.FindAllAuthorsInPort
import com.example.librarymanagement.author.infrastructure.convertor.AuthorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllAuthorsNatsController(
    private val findAllAuthorsInPort: FindAllAuthorsInPort,
    private val authorMapper: AuthorMapper
) : NatsController<FindAllAuthorsRequest, FindAllAuthorsResponse> {
    override val subject = FIND_ALL
    override val parser: Parser<FindAllAuthorsRequest> = FindAllAuthorsRequest.parser()

    override fun handle(request: FindAllAuthorsRequest): Mono<FindAllAuthorsResponse> {
        return findAllAuthorsInPort.findAllAuthors()
            .map { authorMapper.toAuthorProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(author: List<Author>): FindAllAuthorsResponse {
        return FindAllAuthorsResponse.newBuilder()
            .apply { successBuilder.authorsBuilder.addAllAuthors(author) }
            .build()
    }
}
