package com.example.com.example.gateway.controller.grpc.journal

import com.example.gateway.publisher.journal.FindAllJournalsByUserIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllByUserIdServiceGrpc
import com.example.internalapi.request.journal.find_all_by_user_id.proto.FindAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.find_all_by_user_id.proto.FindAllJournalsByUserIdResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllJournalsByUserIdService(
    private val journalsByUserIdNatsPublisher: FindAllJournalsByUserIdNatsPublisher
) : ReactorFindAllByUserIdServiceGrpc.FindAllByUserIdServiceImplBase() {

    override fun findAllByUserId(request: Mono<FindAllJournalsByUserIdRequest>): Mono<FindAllJournalsByUserIdResponse> =
        request.flatMap { journal -> journalsByUserIdNatsPublisher.request(journal) }
}
