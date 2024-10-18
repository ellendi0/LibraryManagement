package com.example.com.example.gateway.controller.grpc.author

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.AuthorRequestValidation
import com.example.gateway.publisher.author.UpdateAuthorNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorUpdateAuthorServiceGrpc
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorResponse
import reactor.core.publisher.Mono

@GrpcService
class UpdateAuthorService(
    private val updateAuthorNatsAuthor: UpdateAuthorNatsPublisher,
    private val validator: AuthorRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorUpdateAuthorServiceGrpc.UpdateAuthorServiceImplBase() {

    override fun updateAuthor(request: Mono<UpdateAuthorRequest>): Mono<UpdateAuthorResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { author -> updateAuthorNatsAuthor.request(author) }
            .onErrorResume {
                Mono.just(UpdateAuthorResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
