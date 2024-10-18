package com.example.com.example.gateway.controller.grpc.bookpresence

import com.example.gateway.publisher.bookpresence.AddBookToLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorAddBookPresenceToLibraryServiceGrpc
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryRequest
import com.example.internalapi.request.book_presence.add_to_library.proto.AddBookToLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class AddBookPresenceToLibraryService(
    private val addBookToLibraryNatsPublisher: AddBookToLibraryNatsPublisher,
) : ReactorAddBookPresenceToLibraryServiceGrpc.AddBookPresenceToLibraryServiceImplBase() {

    override fun addToLibrary(request: Mono<AddBookToLibraryRequest>): Mono<AddBookToLibraryResponse> =
        request.flatMap { bookPresence -> addBookToLibraryNatsPublisher.request(bookPresence) }
}
