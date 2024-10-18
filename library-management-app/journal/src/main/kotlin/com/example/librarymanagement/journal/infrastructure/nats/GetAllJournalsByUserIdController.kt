package com.example.librarymanagement.journal.infrastructure.nats

import com.example.internalapi.NatsSubject.Journal.FIND_ALL_BY_USER_ID
import com.example.internalapi.model.Journal
import com.example.internalapi.request.journal.find_all_by_user_id.proto.FindAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.find_all_by_user_id.proto.FindAllJournalsByUserIdResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.journal.application.port.`in`.FindAllJournalsByUserIdInPort
import com.example.librarymanagement.journal.infrastructure.convertor.JournalMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllJournalsByUserIdController(
    private val journalService: FindAllJournalsByUserIdInPort,
    private val journalMapper: JournalMapper,
) : NatsController<FindAllJournalsByUserIdRequest, FindAllJournalsByUserIdResponse> {
    override val subject = FIND_ALL_BY_USER_ID
    override val parser: Parser<FindAllJournalsByUserIdRequest> = FindAllJournalsByUserIdRequest.parser()

    override fun handle(request: FindAllJournalsByUserIdRequest): Mono<FindAllJournalsByUserIdResponse> {
        return journalService.findAllJournalsByUserId(request.userId)
            .map { journalMapper.toJournalProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(journals: List<Journal>): FindAllJournalsByUserIdResponse {
        return FindAllJournalsByUserIdResponse.newBuilder()
            .apply { successBuilder.journalsBuilder.addAllJournals(journals) }
            .build()
    }
}
