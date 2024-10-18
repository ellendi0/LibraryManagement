package com.example.gateway.publisher.reservation

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.reservation.find_all_by_user_id.proto.FindAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.find_all_by_user_id.proto.FindAllReservationsByUserIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllReservationsByUserIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllReservationsByUserIdRequest, FindAllReservationsByUserIdResponse> {
    override val subject = NatsSubject.Reservation.FIND_ALL_BY_USER_ID
    override val parser: Parser<FindAllReservationsByUserIdResponse> = FindAllReservationsByUserIdResponse.parser()

    override fun request(request: FindAllReservationsByUserIdRequest): Mono<FindAllReservationsByUserIdResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
