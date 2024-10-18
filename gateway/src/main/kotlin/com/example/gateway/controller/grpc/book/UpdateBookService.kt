package com.example.com.example.gateway.controller.grpc.book

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.BookRequestValidation
import com.example.gateway.publisher.book.UpdateBookNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorUpdateBookServiceGrpc
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookResponse
import reactor.core.publisher.Mono

@GrpcService
class UpdateBookService(
    private val updateBookNatsBook: UpdateBookNatsPublisher,
    private val validator: BookRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorUpdateBookServiceGrpc.UpdateBookServiceImplBase() {

    override fun updateBook(request: Mono<UpdateBookRequest>): Mono<UpdateBookResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { book -> updateBookNatsBook.request(book) }
            .onErrorResume {
                Mono.just(UpdateBookResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
