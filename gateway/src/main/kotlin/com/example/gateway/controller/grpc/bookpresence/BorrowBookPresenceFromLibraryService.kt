package com.example.com.example.gateway.controller.grpc.bookpresence

import com.example.gateway.publisher.bookpresence.BorrowBookFromLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorBorrowBookFromLibraryServiceGrpc
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryRequest
import com.example.internalapi.request.book_presence.borrow_from_library.proto.BorrowBookFromLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class BorrowBookPresenceFromLibraryService(
    private val borrowBookFromLibraryNatsPublisher: BorrowBookFromLibraryNatsPublisher,
) : ReactorBorrowBookFromLibraryServiceGrpc.BorrowBookFromLibraryServiceImplBase() {

    override fun borrowFromLibrary(request: Mono<BorrowBookFromLibraryRequest>): Mono<BorrowBookFromLibraryResponse> =
        request.flatMap { bookPresence -> borrowBookFromLibraryNatsPublisher.request(bookPresence) }
}
