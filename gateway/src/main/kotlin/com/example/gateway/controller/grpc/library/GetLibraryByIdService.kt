package com.example.com.example.gateway.controller.grpc.library

import com.example.gateway.publisher.library.GetLibraryByIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorGetLibraryByIdServiceGrpc
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdRequest
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdResponse
import reactor.core.publisher.Mono

@GrpcService
class GetLibraryByIdService(
    private val getLibraryByIdNatsLibrary: GetLibraryByIdNatsPublisher,
) : ReactorGetLibraryByIdServiceGrpc.GetLibraryByIdServiceImplBase() {

    override fun getLibraryById(request: Mono<GetLibraryByIdRequest>): Mono<GetLibraryByIdResponse> {
        return request.flatMap { book ->
            getLibraryByIdNatsLibrary.request(
                GetLibraryByIdRequest.newBuilder().apply { id = book.id }.build()
            )
        }
    }
}
