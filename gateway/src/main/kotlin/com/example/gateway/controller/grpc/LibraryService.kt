package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.LibraryRequestValidation
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.library.CreateLibraryNatsPublisher
import com.example.gateway.publisher.library.DeleteLibraryNatsPublisher
import com.example.gateway.publisher.library.GetAllLibraryNatsPublisher
import com.example.gateway.publisher.library.GetLibraryByIdNatsPublisher
import com.example.gateway.publisher.library.UpdateLibraryNatsPublisher
import com.example.internalapi.ReactorLibraryServiceGrpc
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse.Failure
import com.example.internalapi.request.library.delete.proto.DeleteLibraryRequest
import com.example.internalapi.request.library.delete.proto.DeleteLibraryResponse
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesRequest
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesResponse
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdRequest
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdResponse
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class LibraryService(
    private val createLibraryNatsLibrary: CreateLibraryNatsPublisher,
    private val getAllLibrariesNatsLibrary: GetAllLibraryNatsPublisher,
    private val getLibraryByIdNatsLibrary: GetLibraryByIdNatsPublisher,
    private val updateLibraryNatsLibrary: UpdateLibraryNatsPublisher,
    private val deleteLibraryNatsLibrary: DeleteLibraryNatsPublisher,
    private val validator: LibraryRequestValidation
) : ReactorLibraryServiceGrpc.LibraryServiceImplBase() {

    override fun createLibrary(request: Mono<CreateLibraryRequest>): Mono<CreateLibraryResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { library -> createLibraryNatsLibrary.request(library) }
            .flatMap {
                if (it.hasSuccess()) handleCreationSuccess(it) else Mono.error(handleCreationFailure(it.failure))
            }
    }

    override fun getAllLibraries(request: Mono<GetAllLibrariesRequest>): Mono<GetAllLibrariesResponse> {
        return request
            .flatMap { library -> getAllLibrariesNatsLibrary.request(library) }
            .flatMap { handleGetAllLibrariesSuccess(it) }
    }

    override fun getLibraryById(request: Mono<GetLibraryByIdRequest>): Mono<GetLibraryByIdResponse> {
        return request
            .flatMap { library ->
                getLibraryByIdNatsLibrary.request(
                    GetLibraryByIdRequest.newBuilder().apply { id = library.id }.build()
                )
            }
            .flatMap {
                if (it.hasSuccess()) {
                    handleGetLibraryByIdSuccess(it)
                } else {
                    Mono.error(handleGetLibraryByIdFailure(it.failure))
                }
            }
    }

    override fun updateLibrary(request: Mono<UpdateLibraryRequest>): Mono<UpdateLibraryResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { library -> updateLibraryNatsLibrary.request(library) }
            .flatMap {
                if (it.hasSuccess()) {
                    Mono.just(handleUpdateLibrarySuccess(it))
                } else {
                    Mono.error(handleUpdateLibraryFailure(it.failure))
                }
            }
    }

    override fun deleteLibrary(request: Mono<DeleteLibraryRequest>): Mono<DeleteLibraryResponse> {
        return request.flatMap { library -> deleteLibraryNatsLibrary.request(library) }
    }

    private fun handleGetAllLibrariesSuccess(response: GetAllLibrariesResponse): Mono<GetAllLibrariesResponse> {
        return GetAllLibrariesResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationSuccess(response: CreateLibraryResponse): Mono<CreateLibraryResponse> {
        return CreateLibraryResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleGetLibraryByIdSuccess(response: GetLibraryByIdResponse): Mono<GetLibraryByIdResponse> {
        return GetLibraryByIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleGetLibraryByIdFailure(failure: GetLibraryByIdResponse.Failure): Exception {
        return when (failure.errorCase) {
            GetLibraryByIdResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            GetLibraryByIdResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            GetLibraryByIdResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            GetLibraryByIdResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleUpdateLibrarySuccess(response: UpdateLibraryResponse): UpdateLibraryResponse {
        return UpdateLibraryResponse.newBuilder().setSuccess(response.success).build()
    }

    private fun handleUpdateLibraryFailure(failure: UpdateLibraryResponse.Failure): Exception {
        return when (failure.errorCase) {
            UpdateLibraryResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            UpdateLibraryResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            UpdateLibraryResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            UpdateLibraryResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }
}
