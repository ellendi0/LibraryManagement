package com.example.com.example.gateway.controller.grpc.library

import com.example.gateway.publisher.library.DeleteLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorDeleteLibraryServiceGrpc
import com.example.internalapi.request.library.delete.proto.DeleteLibraryRequest
import com.example.internalapi.request.library.delete.proto.DeleteLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class DeleteLibraryService(
    private val deleteLibraryNatsLibrary: DeleteLibraryNatsPublisher
) : ReactorDeleteLibraryServiceGrpc.DeleteLibraryServiceImplBase() {

    override fun deleteLibrary(request: Mono<DeleteLibraryRequest>): Mono<DeleteLibraryResponse> =
        request.flatMap { library -> deleteLibraryNatsLibrary.request(library) }
}
