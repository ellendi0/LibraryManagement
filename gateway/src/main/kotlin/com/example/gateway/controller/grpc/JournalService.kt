package com.example.com.example.gateway.controller.grpc

import com.example.gateway.publisher.journal.GetAllJournalsByUserIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorJournalServiceGrpc
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdResponse
import reactor.core.publisher.Mono

@GrpcService
class JournalService(
    private val journalsByUserIdNatsPublisher: GetAllJournalsByUserIdNatsPublisher
) : ReactorJournalServiceGrpc.JournalServiceImplBase() {

    override fun getAllByUserId(request: Mono<GetAllJournalsByUserIdRequest>): Mono<GetAllJournalsByUserIdResponse> =
        request.flatMap { journal -> journalsByUserIdNatsPublisher.request(journal) }
}
