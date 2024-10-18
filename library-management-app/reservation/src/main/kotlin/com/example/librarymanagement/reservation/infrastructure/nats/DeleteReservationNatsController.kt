package com.example.librarymanagement.reservation.infrastructure.nats

import com.example.internalapi.NatsSubject.Reservation.DELETE
import com.example.internalapi.request.reservation.delete.proto.CancelReservationRequest
import com.example.internalapi.request.reservation.delete.proto.CancelReservationResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.reservation.application.port.`in`.CancelReservationInPort
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteReservationNatsController(
    private val cancelReservationInPort: CancelReservationInPort,
) : NatsController<CancelReservationRequest, CancelReservationResponse> {
    override val subject = DELETE
    override val parser: Parser<CancelReservationRequest> = CancelReservationRequest.parser()

    override fun handle(request: CancelReservationRequest): Mono<CancelReservationResponse> {
        return cancelReservationInPort.cancelReservation(request.userId, request.bookId)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): CancelReservationResponse {
        return CancelReservationResponse.newBuilder().apply { successBuilder }.build()
    }
}
