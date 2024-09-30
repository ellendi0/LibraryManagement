package com.example.com.example.gateway.controller.grpc

import com.example.gateway.publisher.journal.GetAllJournalsByUserIdNatsPublisher
import com.example.internalapi.ReactorJournalServiceGrpc
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class JournalService(
    private val journalsByUserIdNatsPublisher: GetAllJournalsByUserIdNatsPublisher
) : ReactorJournalServiceGrpc.JournalServiceImplBase() {
    override fun getAllByUserId(request: Mono<GetAllJournalsByUserIdRequest>): Mono<GetAllJournalsByUserIdResponse> {
        return request
            .flatMap { journal -> journalsByUserIdNatsPublisher.request(journal) }
            .flatMap { handleSuccess(it) }
    }

    private fun handleSuccess(response: GetAllJournalsByUserIdResponse): Mono<GetAllJournalsByUserIdResponse> {
        return GetAllJournalsByUserIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }
}
