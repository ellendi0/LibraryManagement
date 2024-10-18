package com.example.com.example.gateway.controller.grpc.book

import com.example.gateway.publisher.book.GetBookByIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorGetBookByIdServiceGrpc
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdRequest
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdResponse
import reactor.core.publisher.Mono

@GrpcService
class GetBookByIdService(
    private val getBookByIdNatsBook: GetBookByIdNatsPublisher,
) : ReactorGetBookByIdServiceGrpc.GetBookByIdServiceImplBase() {

    override fun getBookById(request: Mono<GetBookByIdRequest>): Mono<GetBookByIdResponse> {
        return request.flatMap { book ->
            getBookByIdNatsBook.request(
                GetBookByIdRequest.newBuilder().apply { id = book.id }.build()
            )
        }
    }
}
