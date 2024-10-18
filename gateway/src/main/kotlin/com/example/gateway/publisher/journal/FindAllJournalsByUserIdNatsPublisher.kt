package com.example.gateway.publisher.journal

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.journal.find_all_by_user_id.proto.FindAllJournalsByUserIdRequest
import com.example.internalapi.request.journal.find_all_by_user_id.proto.FindAllJournalsByUserIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllJournalsByUserIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllJournalsByUserIdRequest, FindAllJournalsByUserIdResponse> {
    override val subject = NatsSubject.Journal.FIND_ALL_BY_USER_ID
    override val parser: Parser<FindAllJournalsByUserIdResponse> = FindAllJournalsByUserIdResponse.parser()

    override fun request(request: FindAllJournalsByUserIdRequest): Mono<FindAllJournalsByUserIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
