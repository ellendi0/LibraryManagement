package com.example.librarymanagement.controller.nats.author

import com.example.internalapi.NatsSubject.Author.GET_ALL
import com.example.internalapi.model.Author
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsRequest
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.AuthorMapper
import com.example.librarymanagement.service.AuthorService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllAuthorsNatsController(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper
) : NatsController<GetAllAuthorsRequest, GetAllAuthorsResponse> {
    override val subject = GET_ALL
    override val parser: Parser<GetAllAuthorsRequest> = GetAllAuthorsRequest.parser()

    override fun handle(request: GetAllAuthorsRequest): Mono<GetAllAuthorsResponse> {
        return authorService.getAllAuthors()
            .map { authorMapper.toAuthorProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(author: List<Author>): GetAllAuthorsResponse {
        return GetAllAuthorsResponse.newBuilder()
            .apply { successBuilder.authorsBuilder.addAllAuthors(author) }
            .build()
    }
}
