package com.example.gateway.publisher.reservation

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllReservationByUserIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllReservationsByUserIdRequest, GetAllReservationsByUserIdResponse> {
    override val subject = NatsSubject.Reservation.GET_ALL_BY_USER_ID
    override val parser: Parser<GetAllReservationsByUserIdResponse> = GetAllReservationsByUserIdResponse.parser()

    override fun request(request: GetAllReservationsByUserIdRequest): Mono<GetAllReservationsByUserIdResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
