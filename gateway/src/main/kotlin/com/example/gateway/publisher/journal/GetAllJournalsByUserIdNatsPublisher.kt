package com.example.gateway.publisher.journal

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.get_all_by_user_id.proto.GetAllJournalsByUserIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllJournalsByUserIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllJournalsByUserIdRequest, GetAllJournalsByUserIdResponse> {
    override val subject = NatsSubject.Journal.GET_ALL_BY_USER_ID
    override val parser: Parser<GetAllJournalsByUserIdResponse> = GetAllJournalsByUserIdResponse.parser()

    override fun request(request: GetAllJournalsByUserIdRequest): Mono<GetAllJournalsByUserIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
