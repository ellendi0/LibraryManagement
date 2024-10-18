package com.example.com.example.gateway.controller.grpc.library

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.LibraryRequestValidation
import com.example.gateway.publisher.library.CreateLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorCreateLibraryServiceGrpc
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class CreateLibraryService(
    private val createLibraryNatsLibrary: CreateLibraryNatsPublisher,
    private val validator: LibraryRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorCreateLibraryServiceGrpc.CreateLibraryServiceImplBase() {

    override fun createLibrary(request: Mono<CreateLibraryRequest>): Mono<CreateLibraryResponse> =
        request
            .flatMap { validator.validate(it) }
            .flatMap { library -> createLibraryNatsLibrary.request(library) }
            .onErrorResume {
                Mono.just(CreateLibraryResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
}
