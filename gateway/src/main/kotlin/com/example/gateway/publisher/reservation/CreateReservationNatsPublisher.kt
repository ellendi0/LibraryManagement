package com.example.gateway.publisher.reservation

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.internalapi.request.reservation.create.proto.CreateReservationResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateReservationNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CreateReservationRequest, CreateReservationResponse> {
    override val subject = NatsSubject.Reservation.CREATE
    override val parser: Parser<CreateReservationResponse> = CreateReservationResponse.parser()

    override fun request(request: CreateReservationRequest): Mono<CreateReservationResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
