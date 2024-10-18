package com.example.com.example.gateway.controller.grpc.book

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.BookRequestValidation
import com.example.gateway.publisher.book.CreateBookNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorCreateBookServiceGrpc
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import reactor.core.publisher.Mono

@GrpcService
class CreateBookService(
    private val createBookNatsBook: CreateBookNatsPublisher,
    private val validator: BookRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorCreateBookServiceGrpc.CreateBookServiceImplBase() {

    override fun createBook(request: Mono<CreateBookRequest>): Mono<CreateBookResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { book -> createBookNatsBook.request(book) }
            .onErrorResume {
                Mono.just(CreateBookResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
    }
}
