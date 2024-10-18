package com.example.com.example.gateway.controller.grpc.book

import com.example.gateway.publisher.book.DeleteBookNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorDeleteBookServiceGrpc
import com.example.internalapi.request.book.delete.proto.DeleteBookRequest
import com.example.internalapi.request.book.delete.proto.DeleteBookResponse
import reactor.core.publisher.Mono

@GrpcService
class DeleteBookService(
    private val deleteBookNatsBook: DeleteBookNatsPublisher,
) : ReactorDeleteBookServiceGrpc.DeleteBookServiceImplBase() {

    override fun deleteBook(request: Mono<DeleteBookRequest>): Mono<DeleteBookResponse> =
        request.flatMap { book -> deleteBookNatsBook.request(book) }
}
