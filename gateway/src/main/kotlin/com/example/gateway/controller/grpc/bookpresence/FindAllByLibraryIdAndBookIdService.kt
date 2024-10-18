package com.example.com.example.gateway.controller.grpc.bookpresence

import com.example.gateway.publisher.bookpresence.FindAllByLibraryIdAndBookIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllByLibraryIdAndBookIdServiceGrpc
import com.example.internalapi.request.book_presence.find_all_by_library_id_and_book_id.proto.FindAllBooksByLibraryIdAndBookIdRequest
import com.example.internalapi.request.book_presence.find_all_by_library_id_and_book_id.proto.FindAllBooksByLibraryIdAndBookIdResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllByLibraryIdAndBookIdService(
    private val findAllByLibraryIdAndBookIdNatsPublisher: FindAllByLibraryIdAndBookIdNatsPublisher,
) : ReactorFindAllByLibraryIdAndBookIdServiceGrpc.FindAllByLibraryIdAndBookIdServiceImplBase() {
    override fun findAllByLibraryIdAndBookId(
        request: Mono<FindAllBooksByLibraryIdAndBookIdRequest>
    ): Mono<FindAllBooksByLibraryIdAndBookIdResponse> =
        request.flatMap { bookPresence -> findAllByLibraryIdAndBookIdNatsPublisher.request(bookPresence) }
}
