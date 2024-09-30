package com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.PublisherRequestValidation
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.publisher.CreatePublisherNatsPublisher
import com.example.gateway.publisher.publisher.GetAllPublishersNatsPublisher
import com.example.gateway.publisher.publisher.GetPublisherByIdNatsPublisher
import com.example.gateway.publisher.publisher.UpdatePublisherNatsPublisher
import com.example.internalapi.ReactorPublisherServiceGrpc
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse.Failure
import com.example.internalapi.request.publisher.get_all.proto.GetAllPublishersRequest
import com.example.internalapi.request.publisher.get_all.proto.GetAllPublishersResponse
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdResponse
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class PublisherService(
    private val createPublisherNatsPublisher: CreatePublisherNatsPublisher,
    private val getAllPublishersNatsPublisher: GetAllPublishersNatsPublisher,
    private val getPublisherByIdNatsPublisher: GetPublisherByIdNatsPublisher,
    private val updatePublisherNatsPublisher: UpdatePublisherNatsPublisher,
    private val validation: PublisherRequestValidation
) : ReactorPublisherServiceGrpc.PublisherServiceImplBase() {

    override fun createPublisher(request: Mono<CreatePublisherRequest>): Mono<CreatePublisherResponse> {
        return request
            .flatMap { validation.validate(it) }
            .flatMap { publisher -> createPublisherNatsPublisher.request(publisher) }
            .flatMap {
                if (it.hasSuccess()) handleCreationSuccess(it) else Mono.error(handleCreationFailure(it.failure))
            }
    }

    override fun getAllPublishers(request: Mono<GetAllPublishersRequest>): Mono<GetAllPublishersResponse> {
        return request
            .flatMap { publisher -> getAllPublishersNatsPublisher.request(publisher) }
            .flatMap { handleGetAllPublishersSuccess(it) }
    }

    override fun getPublisherById(request: Mono<GetPublisherByIdRequest>): Mono<GetPublisherByIdResponse> {
        return request
            .flatMap { publisher ->
                getPublisherByIdNatsPublisher.request(
                    GetPublisherByIdRequest.newBuilder().apply { id = publisher.id }.build()
                )
            }
            .flatMap {
                if (it.hasSuccess()) {
                    handleGetPublisherByIdSuccess(it)
                } else {
                    Mono.error(handleGetPublisherByIdFailure(it.failure))
                }
            }
    }

    override fun updatePublisher(request: Mono<UpdatePublisherRequest>): Mono<UpdatePublisherResponse> {
        return request
            .flatMap { validation.validate(it) }
            .flatMap { publisher -> updatePublisherNatsPublisher.request(publisher) }
            .flatMap {
                if (it.hasSuccess()) {
                    Mono.just(handleUpdatePublisherSuccess(it))
                } else {
                    Mono.error(handleUpdatePublisherFailure(it.failure))
                }
            }
    }

    private fun handleGetAllPublishersSuccess(response: GetAllPublishersResponse): Mono<GetAllPublishersResponse> {
        return GetAllPublishersResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationSuccess(response: CreatePublisherResponse): Mono<CreatePublisherResponse> {
        return CreatePublisherResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleGetPublisherByIdSuccess(response: GetPublisherByIdResponse): Mono<GetPublisherByIdResponse> {
        return GetPublisherByIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleGetPublisherByIdFailure(failure: GetPublisherByIdResponse.Failure): Exception {
        return when (failure.errorCase) {
            GetPublisherByIdResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            GetPublisherByIdResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            GetPublisherByIdResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            GetPublisherByIdResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleUpdatePublisherSuccess(response: UpdatePublisherResponse): UpdatePublisherResponse {
        return UpdatePublisherResponse.newBuilder().setSuccess(response.success).build()
    }

    private fun handleUpdatePublisherFailure(failure: UpdatePublisherResponse.Failure): Exception {
        return when (failure.errorCase) {
            UpdatePublisherResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            UpdatePublisherResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            UpdatePublisherResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            UpdatePublisherResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }
}
