package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.exception.DuplicateKeyException
import com.example.com.example.gateway.validation.BookRequestValidation
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.book.CreateBookNatsPublisher
import com.example.gateway.publisher.book.DeleteBookNatsPublisher
import com.example.gateway.publisher.book.GetAllBookNatsPublisher
import com.example.gateway.publisher.book.GetBookByIdNatsPublisher
import com.example.gateway.publisher.book.GetBookByTitleAndAuthorNatsPublisher
import com.example.gateway.publisher.book.UpdateBookNatsPublisher
import com.example.internalapi.ReactorBookServiceGrpc
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import com.example.internalapi.request.book.create.proto.CreateBookResponse.Failure
import com.example.internalapi.request.book.delete.proto.DeleteBookRequest
import com.example.internalapi.request.book.delete.proto.DeleteBookResponse
import com.example.internalapi.request.book.get_all.proto.GetAllBooksRequest
import com.example.internalapi.request.book.get_all.proto.GetAllBooksResponse
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdRequest
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdResponse
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorRequest
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorResponse
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class BookService(
    private val createBookNatsBook: CreateBookNatsPublisher,
    private val getAllBooksNatsBook: GetAllBookNatsPublisher,
    private val getBookByIdNatsBook: GetBookByIdNatsPublisher,
    private val updateBookNatsBook: UpdateBookNatsPublisher,
    private val deleteBookNatsBook: DeleteBookNatsPublisher,
    private val getBookByTitleAndAuthorNatsPublisher: GetBookByTitleAndAuthorNatsPublisher,
    private val validator: BookRequestValidation
) : ReactorBookServiceGrpc.BookServiceImplBase() {

    override fun createBook(request: Mono<CreateBookRequest>): Mono<CreateBookResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { book -> createBookNatsBook.request(book) }
            .flatMap {
                if (it.hasSuccess()) handleCreationSuccess(it) else Mono.error(handleCreationFailure(it.failure))
            }
    }

    override fun getAllBooks(request: Mono<GetAllBooksRequest>): Mono<GetAllBooksResponse> {
        return request
            .flatMap { book -> getAllBooksNatsBook.request(book) }
            .flatMap { handleGetAllBooksSuccess(it) }
    }

    override fun getBookById(request: Mono<GetBookByIdRequest>): Mono<GetBookByIdResponse> {
        return request
            .flatMap { book ->
                getBookByIdNatsBook.request(
                    GetBookByIdRequest.newBuilder().apply { id = book.id }.build()
                )
            }
            .flatMap { response ->
                if (response.hasSuccess()) {
                    handleGetBookByIdSuccess(response)
                } else {
                    Mono.error(handleGetBookByIdFailure(response.failure))
                }
            }
    }


    override fun updateBook(request: Mono<UpdateBookRequest>): Mono<UpdateBookResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { book -> updateBookNatsBook.request(book) }
            .flatMap {
                if (it.hasSuccess()) {
                    Mono.just(handleUpdateBookSuccess(it))
                } else {
                    Mono.error(handleUpdateBookFailure(it.failure))
                }
            }
    }

    override fun deleteBook(request: Mono<DeleteBookRequest>): Mono<DeleteBookResponse> {
        return request.flatMap { book -> deleteBookNatsBook.request(book) }
    }

    override fun getBookByTitleAndAuthor(request: Mono<GetBookByTitleAndAuthorRequest>): Mono<GetBookByTitleAndAuthorResponse> {
        return request
            .flatMap { book -> getBookByTitleAndAuthorNatsPublisher.request(book) }
            .flatMap {
                if (it.hasSuccess()) {
                    Mono.just(handleGetBookByTitleAndAuthorSuccess(it))
                } else {
                    Mono.error(handleGetBookByTitleAndAuthorFailure(it.failure))
                }
            }
    }

    private fun handleGetAllBooksSuccess(response: GetAllBooksResponse): Mono<GetAllBooksResponse> {
        return GetAllBooksResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationSuccess(response: CreateBookResponse): Mono<CreateBookResponse> {
        return CreateBookResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            Failure.ErrorCase.DUPLICATE_KEY_ERROR -> DuplicateKeyException(failure.duplicateKeyError.messages)
            null -> IllegalStateException("Error case is null")
            Failure.ErrorCase.NOT_FOUND_ERROR -> EntityNotFoundException(failure.notFoundError.messages)
            Failure.ErrorCase.ILLEGAL_ARGUMENT_ERROR -> IllegalArgumentException(failure.illegalArgumentError.messages)
        }
    }

    private fun handleGetBookByIdSuccess(response: GetBookByIdResponse): Mono<GetBookByIdResponse> {
        return GetBookByIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleGetBookByIdFailure(failure: GetBookByIdResponse.Failure): Exception {
        return when (failure.errorCase) {
            GetBookByIdResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            GetBookByIdResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            GetBookByIdResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            GetBookByIdResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleUpdateBookSuccess(response: UpdateBookResponse): UpdateBookResponse {
        return UpdateBookResponse.newBuilder().setSuccess(response.success).build()
    }

    private fun handleUpdateBookFailure(failure: UpdateBookResponse.Failure): Exception {
        return when (failure.errorCase) {
            UpdateBookResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            UpdateBookResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_ERROR ->
                IllegalArgumentException(failure.illegalArgumentError.messages)

            UpdateBookResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            UpdateBookResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            UpdateBookResponse.Failure.ErrorCase.DUPLICATE_KEY_ERROR -> DuplicateKeyException(failure.duplicateKeyError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleGetBookByTitleAndAuthorSuccess(
        response: GetBookByTitleAndAuthorResponse
    ): GetBookByTitleAndAuthorResponse {
        return GetBookByTitleAndAuthorResponse.newBuilder().setSuccess(response.success).build()
    }

    private fun handleGetBookByTitleAndAuthorFailure(
        failure: GetBookByTitleAndAuthorResponse.Failure
    ): Exception {
        return when (failure.errorCase) {
            GetBookByTitleAndAuthorResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            GetBookByTitleAndAuthorResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(failure.illegalArgumentExpression.messages)

            GetBookByTitleAndAuthorResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            GetBookByTitleAndAuthorResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            null -> IllegalStateException("Error case is null")
        }
    }
}
