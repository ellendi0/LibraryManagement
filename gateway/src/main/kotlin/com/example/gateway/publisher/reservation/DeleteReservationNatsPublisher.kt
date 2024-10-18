package com.example.gateway.publisher.reservation

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.reservation.delete.proto.CancelReservationRequest
import com.example.internalapi.request.reservation.delete.proto.CancelReservationResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteReservationNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CancelReservationRequest, CancelReservationResponse> {
    override val subject = NatsSubject.Reservation.DELETE
    override val parser: Parser<CancelReservationResponse> = CancelReservationResponse.parser()

    override fun request(request: CancelReservationRequest): Mono<CancelReservationResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
