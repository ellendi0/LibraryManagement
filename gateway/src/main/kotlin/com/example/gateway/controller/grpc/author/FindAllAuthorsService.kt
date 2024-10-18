package com.example.com.example.gateway.controller.grpc.author

import com.example.gateway.publisher.author.FindAllAuthorNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllAuthorsServiceGrpc
import com.example.internalapi.request.author.find_all.proto.FindAllAuthorsRequest
import com.example.internalapi.request.author.find_all.proto.FindAllAuthorsResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllAuthorsService(
    private val findAllAuthorsNatsAuthor: FindAllAuthorNatsPublisher,
) : ReactorFindAllAuthorsServiceGrpc.FindAllAuthorsServiceImplBase() {

    override fun findAllAuthors(request: Mono<FindAllAuthorsRequest>): Mono<FindAllAuthorsResponse> {
        return request.flatMap { author -> findAllAuthorsNatsAuthor.request(author) }
    }
}
