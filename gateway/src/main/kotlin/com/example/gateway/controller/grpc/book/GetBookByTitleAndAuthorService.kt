package com.example.com.example.gateway.controller.grpc.book

import com.example.gateway.publisher.book.GetBookByTitleAndAuthorNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorGetBookByTitleAndAuthorServiceGrpc
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorRequest
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorResponse
import reactor.core.publisher.Mono

@GrpcService
class GetBookByTitleAndAuthorService(
    private val getBookByTitleAndAuthorNatsPublisher: GetBookByTitleAndAuthorNatsPublisher,
) : ReactorGetBookByTitleAndAuthorServiceGrpc.GetBookByTitleAndAuthorServiceImplBase() {

    override fun getBookByTitleAndAuthor(
        request: Mono<GetBookByTitleAndAuthorRequest>
    ): Mono<GetBookByTitleAndAuthorResponse> =
        request.flatMap { book -> getBookByTitleAndAuthorNatsPublisher.request(book) }
}
