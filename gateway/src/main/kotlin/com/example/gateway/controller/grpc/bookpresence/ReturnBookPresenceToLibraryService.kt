package com.example.com.example.gateway.controller.grpc.bookpresence

import com.example.gateway.publisher.bookpresence.ReturnBookPresenceToLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorReturnBookToLibraryServiceGrpc
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryRequest
import com.example.internalapi.request.book_presence.return_to_library.proto.ReturnBookToLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class ReturnBookPresenceToLibraryService(
    private val returnBookPresenceToLibraryNatsPublisher: ReturnBookPresenceToLibraryNatsPublisher
) : ReactorReturnBookToLibraryServiceGrpc.ReturnBookToLibraryServiceImplBase() {

    override fun returnToLibrary(request: Mono<ReturnBookToLibraryRequest>): Mono<ReturnBookToLibraryResponse> =
        request.flatMap { bookPresence -> returnBookPresenceToLibraryNatsPublisher.request(bookPresence) }
}
