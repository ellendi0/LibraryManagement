package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.AuthorRequestValidation
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.author.CreateAuthorNatsPublisher
import com.example.gateway.publisher.author.GetAllAuthorNatsPublisher
import com.example.gateway.publisher.author.GetAuthorByIdNatsPublisher
import com.example.gateway.publisher.author.UpdateAuthorNatsPublisher
import com.example.internalapi.ReactorAuthorServiceGrpc
import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse.Failure
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsRequest
import com.example.internalapi.request.author.get_all.proto.GetAllAuthorsResponse
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdRequest
import com.example.internalapi.request.author.get_by_id.proto.GetAuthorByIdResponse
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class AuthorService(
    private val createAuthorNatsAuthor: CreateAuthorNatsPublisher,
    private val getAllAuthorsNatsAuthor: GetAllAuthorNatsPublisher,
    private val getAuthorByIdNatsAuthor: GetAuthorByIdNatsPublisher,
    private val updateAuthorNatsAuthor: UpdateAuthorNatsPublisher,
    private val validator: AuthorRequestValidation
) : ReactorAuthorServiceGrpc.AuthorServiceImplBase() {

    override fun createAuthor(request: Mono<CreateAuthorRequest>): Mono<CreateAuthorResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { author -> createAuthorNatsAuthor.request(author) }
            .flatMap {
                if (it.hasSuccess()) handleCreationSuccess(it) else Mono.error(handleCreationFailure(it.failure))
            }
    }

    override fun getAllAuthors(request: Mono<GetAllAuthorsRequest>): Mono<GetAllAuthorsResponse> {
        return request
            .flatMap { author -> getAllAuthorsNatsAuthor.request(author) }
            .flatMap { handleGetAllAuthorsSuccess(it) }
    }

    override fun getAuthorById(request: Mono<GetAuthorByIdRequest>): Mono<GetAuthorByIdResponse> {
        return request
            .flatMap { author ->
                getAuthorByIdNatsAuthor.request(
                    GetAuthorByIdRequest.newBuilder().apply { id = author.id }.build()
                )
            }
            .flatMap {
                if (it.hasSuccess()) {
                    handleGetAuthorByIdSuccess(it)
                } else {
                    Mono.error(handleGetAuthorByIdFailure(it.failure))
                }
            }
    }

    override fun updateAuthor(request: Mono<UpdateAuthorRequest>): Mono<UpdateAuthorResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { author -> updateAuthorNatsAuthor.request(author) }
            .flatMap {
                if (it.hasSuccess()) {
                    Mono.just(handleUpdateAuthorSuccess(it))
                } else {
                    Mono.error(handleUpdateAuthorFailure(it.failure))
                }
            }
    }

    private fun handleGetAllAuthorsSuccess(response: GetAllAuthorsResponse): Mono<GetAllAuthorsResponse> {
        return GetAllAuthorsResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationSuccess(response: CreateAuthorResponse): Mono<CreateAuthorResponse> {
        return CreateAuthorResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleGetAuthorByIdSuccess(response: GetAuthorByIdResponse): Mono<GetAuthorByIdResponse> {
        return GetAuthorByIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleGetAuthorByIdFailure(failure: GetAuthorByIdResponse.Failure): Exception {
        return when (failure.errorCase) {
            GetAuthorByIdResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            GetAuthorByIdResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            GetAuthorByIdResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            GetAuthorByIdResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleUpdateAuthorSuccess(response: UpdateAuthorResponse): UpdateAuthorResponse {
        return UpdateAuthorResponse.newBuilder().setSuccess(response.success).build()
    }

    private fun handleUpdateAuthorFailure(failure: UpdateAuthorResponse.Failure): Exception {
        return when (failure.errorCase) {
            UpdateAuthorResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            UpdateAuthorResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            UpdateAuthorResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            UpdateAuthorResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }
}
