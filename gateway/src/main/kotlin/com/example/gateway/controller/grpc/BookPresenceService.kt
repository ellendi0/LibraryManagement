package com.example.com.example.gateway.controller.grpc

import com.example.gateway.publisher.bookpresence.AddBookToLibraryNatsPublisher
import com.example.gateway.publisher.bookpresence.BorrowBookFromLibraryNatsPublisher
import com.example.gateway.publisher.bookpresence.GetAllByLibraryIdAndBookIdNatsPublisher
import com.example.gateway.publisher.bookpresence.GetAllByLibraryIdNatsPublisher
import com.example.gateway.publisher.bookpresence.RemoveBookPresenceFromLibraryNatsPublisher
import com.example.gateway.publisher.bookpresence.ReturnBookPresenceToLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
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
import reactor.core.publisher.Mono

@GrpcService
class BookPresenceService(
    private val addBookToLibraryNatsPublisher: AddBookToLibraryNatsPublisher,
    private val borrowBookFromLibraryNatsPublisher: BorrowBookFromLibraryNatsPublisher,
    private val getAllByLibraryIdAndBookIdNatsPublisher: GetAllByLibraryIdAndBookIdNatsPublisher,
    private val getAllByLibraryIdNatsPublisher: GetAllByLibraryIdNatsPublisher,
    private val removeBookPresenceFromLibraryNatsPublisher: RemoveBookPresenceFromLibraryNatsPublisher,
    private val returnBookPresenceToLibraryNatsPublisher: ReturnBookPresenceToLibraryNatsPublisher
) : ReactorBookPresenceServiceGrpc.BookPresenceServiceImplBase() {

    override fun addToLibrary(request: Mono<AddBookToLibraryRequest>): Mono<AddBookToLibraryResponse> =
        request.flatMap { bookPresence -> addBookToLibraryNatsPublisher.request(bookPresence) }

    override fun borrowFromLibrary(request: Mono<BorrowBookFromLibraryRequest>): Mono<BorrowBookFromLibraryResponse> =
        request.flatMap { bookPresence -> borrowBookFromLibraryNatsPublisher.request(bookPresence) }

    override fun getAllByLibraryIdAndBookId(
        request: Mono<GetAllBooksByLibraryIdAndBookIdRequest>
    ): Mono<GetAllBooksByLibraryIdAndBookIdResponse> =
        request.flatMap { bookPresence -> getAllByLibraryIdAndBookIdNatsPublisher.request(bookPresence) }

    override fun getAllByLibraryId(request: Mono<GetAllBooksByLibraryIdRequest>): Mono<GetAllBooksByLibraryIdResponse> =
        request.flatMap { bookPresence -> getAllByLibraryIdNatsPublisher.request(bookPresence) }

    override fun returnToLibrary(request: Mono<ReturnBookToLibraryRequest>): Mono<ReturnBookToLibraryResponse> =
        request.flatMap { bookPresence -> returnBookPresenceToLibraryNatsPublisher.request(bookPresence) }

    override fun removeFromLibrary(request: Mono<RemoveBookFromLibraryRequest>): Mono<RemoveBookFromLibraryResponse> =
        request.flatMap { bookPresence -> removeBookPresenceFromLibraryNatsPublisher.request(bookPresence) }
}
