package com.example.com.example.gateway.controller.grpc.library

import com.example.gateway.publisher.library.FindAllLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllLibrariesServiceGrpc
import com.example.internalapi.request.library.find_all.proto.FindAllLibrariesRequest
import com.example.internalapi.request.library.find_all.proto.FindAllLibrariesResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllLibrariesService(
    private val getAllLibrariesNatsLibrary: FindAllLibraryNatsPublisher,
) : ReactorFindAllLibrariesServiceGrpc.FindAllLibrariesServiceImplBase() {

    override fun findAllLibraries(request: Mono<FindAllLibrariesRequest>): Mono<FindAllLibrariesResponse> =
        request.flatMap { library -> getAllLibrariesNatsLibrary.request(library) }
}
