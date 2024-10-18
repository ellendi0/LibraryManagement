package com.example.com.example.gateway.controller.grpc.bookpresence

import com.example.gateway.publisher.bookpresence.FindAllByLibraryIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllByLibraryIdServiceGrpc
import com.example.internalapi.request.book_presence.find_all_by_library_id.proto.FindAllBooksByLibraryIdRequest
import com.example.internalapi.request.book_presence.find_all_by_library_id.proto.FindAllBooksByLibraryIdResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllByLibraryIdService(
    private val findAllByLibraryIdNatsPublisher: FindAllByLibraryIdNatsPublisher,
) : ReactorFindAllByLibraryIdServiceGrpc.FindAllByLibraryIdServiceImplBase() {
    override fun findAllByLibraryId(request: Mono<FindAllBooksByLibraryIdRequest>): Mono<FindAllBooksByLibraryIdResponse> =
        request.flatMap { bookPresence -> findAllByLibraryIdNatsPublisher.request(bookPresence) }
}
