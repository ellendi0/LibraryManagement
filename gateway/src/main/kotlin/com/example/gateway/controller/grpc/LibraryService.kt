package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.LibraryRequestValidation
import com.example.gateway.publisher.library.CreateLibraryNatsPublisher
import com.example.gateway.publisher.library.DeleteLibraryNatsPublisher
import com.example.gateway.publisher.library.GetAllLibraryNatsPublisher
import com.example.gateway.publisher.library.GetLibraryByIdNatsPublisher
import com.example.gateway.publisher.library.UpdateLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorLibraryServiceGrpc
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import com.example.internalapi.request.library.delete.proto.DeleteLibraryRequest
import com.example.internalapi.request.library.delete.proto.DeleteLibraryResponse
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesRequest
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesResponse
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdRequest
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdResponse
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class LibraryService(
    private val createLibraryNatsLibrary: CreateLibraryNatsPublisher,
    private val getAllLibrariesNatsLibrary: GetAllLibraryNatsPublisher,
    private val getLibraryByIdNatsLibrary: GetLibraryByIdNatsPublisher,
    private val updateLibraryNatsLibrary: UpdateLibraryNatsPublisher,
    private val deleteLibraryNatsLibrary: DeleteLibraryNatsPublisher,
    private val validator: LibraryRequestValidation
) : ReactorLibraryServiceGrpc.LibraryServiceImplBase() {

    override fun createLibrary(request: Mono<CreateLibraryRequest>): Mono<CreateLibraryResponse> =
        request
            .flatMap { validator.validate(it) }
            .flatMap { library -> createLibraryNatsLibrary.request(library) }

    override fun getAllLibraries(request: Mono<GetAllLibrariesRequest>): Mono<GetAllLibrariesResponse> =
        request.flatMap { library -> getAllLibrariesNatsLibrary.request(library) }

    override fun getLibraryById(request: Mono<GetLibraryByIdRequest>): Mono<GetLibraryByIdResponse> =
        request.flatMap { library ->
            getLibraryByIdNatsLibrary.request(
                GetLibraryByIdRequest.newBuilder().apply { id = library.id }.build()
            )
        }

    override fun updateLibrary(request: Mono<UpdateLibraryRequest>): Mono<UpdateLibraryResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { library -> updateLibraryNatsLibrary.request(library) }
    }

    override fun deleteLibrary(request: Mono<DeleteLibraryRequest>): Mono<DeleteLibraryResponse> =
        request.flatMap { library -> deleteLibraryNatsLibrary.request(library) }
}
