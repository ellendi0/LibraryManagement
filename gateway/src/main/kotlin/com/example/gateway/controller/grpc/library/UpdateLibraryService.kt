package com.example.com.example.gateway.controller.grpc.library

import com.example.com.example.gateway.exception.mapper.ErrorMapper
import com.example.com.example.gateway.validation.LibraryRequestValidation
import com.example.gateway.publisher.library.UpdateLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorUpdateLibraryServiceGrpc
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class UpdateLibraryService(
    private val updateLibraryNatsLibrary: UpdateLibraryNatsPublisher,
    private val validator: LibraryRequestValidation,
    private val errorMapper: ErrorMapper
) : ReactorUpdateLibraryServiceGrpc.UpdateLibraryServiceImplBase() {

    override fun updateLibrary(request: Mono<UpdateLibraryRequest>): Mono<UpdateLibraryResponse> =
        request
            .flatMap { validator.validate(it) }
            .flatMap { library -> updateLibraryNatsLibrary.request(library) }
            .onErrorResume {
                Mono.just(UpdateLibraryResponse.newBuilder().apply {
                    failureBuilder.setUnknownError(errorMapper.toErrorProto(it))
                }.build())
            }
}
