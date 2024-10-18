package com.example.com.example.gateway.controller.grpc.author

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.AuthorRequestValidation
import com.example.gateway.publisher.author.CreateAuthorNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorCreateAuthorServiceGrpc
import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse
import reactor.core.publisher.Mono

@GrpcService
class CreateAuthorService(
    private val createAuthorNatsAuthor: CreateAuthorNatsPublisher,
    private val validator: AuthorRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorCreateAuthorServiceGrpc.CreateAuthorServiceImplBase() {

    override fun createAuthor(request: Mono<CreateAuthorRequest>): Mono<CreateAuthorResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { author -> createAuthorNatsAuthor.request(author) }
            .onErrorResume {
                Mono.just(CreateAuthorResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
