package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.exception.BookAvailabilityException
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.bookpresence.AddBookToLibraryNatsPublisher
import com.example.gateway.publisher.bookpresence.BorrowBookFromLibraryNatsPublisher
import com.example.gateway.publisher.bookpresence.GetAllByLibraryIdAndBookIdNatsPublisher
import com.example.gateway.publisher.bookpresence.GetAllByLibraryIdNatsPublisher
import com.example.gateway.publisher.bookpresence.RemoveBookPresenceFromLibraryNatsPublisher
import com.example.gateway.publisher.bookpresence.ReturnBookPresenceToLibraryNatsPublisher
import com.example.internalapi.ReactorBookPresenceServiceGrpc
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryRequest
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryResponse
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryRequest
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryResponse
import com.example.internalapi.request.book_presence.get_all_by_library_id.proto.GetAllBooksByLibraryIdRequest
import com.example.internalapi.request.book_presence.get_all_by_library_id.proto.GetAllBooksByLibraryIdResponse
import com.example.internalapi.request.book_presence.get_all_by_library_id_and_book_id.proto.GetAllBooksByLibraryIdAndBookIdRequest
import com.example.internalapi.request.book_presence.get_all_by_library_id_and_book_id.proto.GetAllBooksByLibraryIdAndBookIdResponse
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryRequest
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryResponse
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryRequest
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class BookPresenceService(
    private val addBookToLibraryNatsPublisher: AddBookToLibraryNatsPublisher,
    private val borrowBookFromLibraryNatsPublisher: BorrowBookFromLibraryNatsPublisher,
    private val getAllByLibraryIdAndBookIdNatsPublisher: GetAllByLibraryIdAndBookIdNatsPublisher,
    private val getAllByLibraryIdNatsPublisher: GetAllByLibraryIdNatsPublisher,
    private val removeBookPresenceFromLibraryNatsPublisher: RemoveBookPresenceFromLibraryNatsPublisher,
    private val returnBookPresenceToLibraryNatsPublisher: ReturnBookPresenceToLibraryNatsPublisher
) : ReactorBookPresenceServiceGrpc.BookPresenceServiceImplBase() {

    override fun addToLibrary(request: Mono<AddBookToLibraryRequest>): Mono<AddBookToLibraryResponse> {
        return request
            .flatMap { bookPresence -> addBookToLibraryNatsPublisher.request(bookPresence) }
            .flatMap {
                if (it.hasSuccess()) {
                    handleAddToLibrarySuccess(it)
                } else {
                    Mono.error(handleAddToLibraryFailure(it.failure))
                }
            }
    }

    override fun borrowFromLibrary(request: Mono<BorrowBookFromLibraryRequest>): Mono<BorrowBookFromLibraryResponse> {
        return request
            .flatMap { bookPresence -> borrowBookFromLibraryNatsPublisher.request(bookPresence) }
            .flatMap {
                if (it.hasSuccess()) {
                    handleBorrowFromLibrarySuccess(it)
                } else {
                    Mono.error(handleBorrowFromLibraryFailure(it.failure))
                }
            }
    }

    override fun getAllByLibraryIdAndBookId(
        request: Mono<GetAllBooksByLibraryIdAndBookIdRequest>
    ): Mono<GetAllBooksByLibraryIdAndBookIdResponse> {
        return request
            .flatMap { bookPresence -> getAllByLibraryIdAndBookIdNatsPublisher.request(bookPresence) }
            .flatMap { GetAllBooksByLibraryIdAndBookIdResponse.newBuilder().setSuccess(it.success).build().toMono() }
    }

    override fun getAllByLibraryId(request: Mono<GetAllBooksByLibraryIdRequest>): Mono<GetAllBooksByLibraryIdResponse> {
        return request
            .flatMap { bookPresence -> getAllByLibraryIdNatsPublisher.request(bookPresence) }
            .flatMap { GetAllBooksByLibraryIdResponse.newBuilder().setSuccess(it.success).build().toMono() }
    }

    override fun returnToLibrary(request: Mono<ReturnBookToLibraryRequest>): Mono<ReturnBookToLibraryResponse> {
        return request
            .flatMap { bookPresence -> returnBookPresenceToLibraryNatsPublisher.request(bookPresence) }
            .flatMap { ReturnBookToLibraryResponse.newBuilder().setSuccess(it.success).build().toMono() }
    }

    override fun removeFromLibrary(request: Mono<RemoveBookFromLibraryRequest>): Mono<RemoveBookFromLibraryResponse> {
        return request
            .flatMap { bookPresence -> removeBookPresenceFromLibraryNatsPublisher.request(bookPresence) }
            .flatMap { RemoveBookFromLibraryResponse.newBuilder().setSuccess(it.success).build().toMono() }
    }

    private fun handleAddToLibrarySuccess(response: AddBookToLibraryResponse): Mono<AddBookToLibraryResponse> {
        return AddBookToLibraryResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleAddToLibraryFailure(failure: AddBookToLibraryResponse.Failure): Exception {
        return when (failure.errorCase) {
            AddBookToLibraryResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            AddBookToLibraryResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(
                    failure.illegalArgumentExpression.messages
                )

            AddBookToLibraryResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            AddBookToLibraryResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            AddBookToLibraryResponse.Failure.ErrorCase.BOOK_AVAILABILITY_ERROR ->
                BookAvailabilityException(failure.bookAvailabilityError.messages)

            null -> IllegalStateException("Error case is null")

        }
    }

    private fun handleBorrowFromLibrarySuccess(
        response: BorrowBookFromLibraryResponse
    ): Mono<BorrowBookFromLibraryResponse> {
        return BorrowBookFromLibraryResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleBorrowFromLibraryFailure(failure: BorrowBookFromLibraryResponse.Failure): Exception {
        return when (failure.errorCase) {
            BorrowBookFromLibraryResponse.Failure.ErrorCase.NOT_FOUND_ERROR ->
                EntityNotFoundException(failure.notFoundError.messages)

            BorrowBookFromLibraryResponse.Failure.ErrorCase.ILLEGAL_ARGUMENT_EXPRESSION ->
                IllegalArgumentException(
                    failure.illegalArgumentExpression.messages
                )

            BorrowBookFromLibraryResponse.Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            BorrowBookFromLibraryResponse.Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            BorrowBookFromLibraryResponse.Failure.ErrorCase.BOOK_AVAILABILITY_ERROR ->
                BookAvailabilityException(failure.bookAvailabilityError.messages)
            null -> IllegalStateException("Error case is null")
        }
    }
}
