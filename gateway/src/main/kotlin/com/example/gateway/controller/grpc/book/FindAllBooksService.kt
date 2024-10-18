package com.example.com.example.gateway.controller.grpc.book

import com.example.gateway.publisher.book.FindAllBookNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllBooksServiceGrpc
import com.example.internalapi.request.book.find_all.proto.FindAllBooksRequest
import com.example.internalapi.request.book.find_all.proto.FindAllBooksResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllBooksService(
    private val getAllBooksNatsBook: FindAllBookNatsPublisher,
) : ReactorFindAllBooksServiceGrpc.FindAllBooksServiceImplBase() {

    override fun findAllBooks(request: Mono<FindAllBooksRequest>): Mono<FindAllBooksResponse> =
        request.flatMap { book -> getAllBooksNatsBook.request(book) }
}
