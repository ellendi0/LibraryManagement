package com.example.com.example.gateway.controller.grpc.bookpresence

import com.example.gateway.publisher.bookpresence.RemoveBookPresenceFromLibraryNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorRemoveBookPresenceFromLibraryGrpc
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryRequest
import com.example.internalapi.request.book_presence.remove_from_library.proto.RemoveBookFromLibraryResponse
import reactor.core.publisher.Mono

@GrpcService
class RemoveBookPresenceFromLibraryService(
    private val removeBookPresenceFromLibraryNatsPublisher: RemoveBookPresenceFromLibraryNatsPublisher,
) : ReactorRemoveBookPresenceFromLibraryGrpc.RemoveBookPresenceFromLibraryImplBase() {

    override fun removeFromLibrary(request: Mono<RemoveBookFromLibraryRequest>): Mono<RemoveBookFromLibraryResponse> =
        request.flatMap { bookPresence -> removeBookPresenceFromLibraryNatsPublisher.request(bookPresence) }
}
