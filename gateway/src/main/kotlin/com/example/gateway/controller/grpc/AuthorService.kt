package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.AuthorRequestValidation
import com.example.gateway.publisher.author.CreateAuthorNatsPublisher
import com.example.gateway.publisher.author.GetAllAuthorNatsPublisher
import com.example.gateway.publisher.author.GetAuthorByIdNatsPublisher
import com.example.gateway.publisher.author.UpdateAuthorNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorAuthorServiceGrpc
import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsRequest
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsResponse
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdRequest
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdResponse
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorResponse
import reactor.core.publisher.Mono

@GrpcService
class AuthorService(
    private val createAuthorNatsAuthor: CreateAuthorNatsPublisher,
    private val getAllAuthorsNatsAuthor: GetAllAuthorNatsPublisher,
    private val getAuthorByIdNatsAuthor: GetAuthorByIdNatsPublisher,
    private val updateAuthorNatsAuthor: UpdateAuthorNatsPublisher,
    private val validator: AuthorRequestValidation
) : ReactorAuthorServiceGrpc.AuthorServiceImplBase() {

    override fun createAuthor(request: Mono<CreateAuthorRequest>): Mono<CreateAuthorResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { author -> createAuthorNatsAuthor.request(author) }
    }

    override fun getAllAuthors(request: Mono<GetAllAuthorsRequest>): Mono<GetAllAuthorsResponse> {
        return request.flatMap { author -> getAllAuthorsNatsAuthor.request(author) }
    }

    override fun getAuthorById(request: Mono<GetAuthorByIdRequest>): Mono<GetAuthorByIdResponse> {
        return request
            .flatMap { author ->
                getAuthorByIdNatsAuthor.request(GetAuthorByIdRequest.newBuilder().apply { id = author.id }.build())
            }
    }

    override fun updateAuthor(request: Mono<UpdateAuthorRequest>): Mono<UpdateAuthorResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { author -> updateAuthorNatsAuthor.request(author) }
    }
}
