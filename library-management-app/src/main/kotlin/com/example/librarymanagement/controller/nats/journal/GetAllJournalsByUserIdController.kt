package com.example.librarymanagement.controller.nats.journal

import com.example.internalapi.NatsSubject.Journal.GET_ALL_BY_USER_ID
import com.example.internalapi.model.Journal
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.JournalMapper
import com.example.librarymanagement.service.JournalService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllJournalsByUserIdController(
    private val journalService: JournalService,
    private val journalMapper: JournalMapper,
) : NatsController<GetAllJournalsByUserIdRequest, GetAllJournalsByUserIdResponse> {
    override val subject = GET_ALL_BY_USER_ID
    override val parser: Parser<GetAllJournalsByUserIdRequest> = GetAllJournalsByUserIdRequest.parser()

    override fun handle(request: GetAllJournalsByUserIdRequest): Mono<GetAllJournalsByUserIdResponse> {
        return journalService.getJournalByUserId(request.userId)
            .map { journalMapper.toJournalProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(journals: List<Journal>): GetAllJournalsByUserIdResponse {
        return GetAllJournalsByUserIdResponse.newBuilder()
            .apply { successBuilder.journalsBuilder.addAllJournals(journals) }
            .build()
    }
}
