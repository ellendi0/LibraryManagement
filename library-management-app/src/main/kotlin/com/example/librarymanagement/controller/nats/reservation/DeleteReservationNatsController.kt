package com.example.reservationmanagement.controller.nats.reservation

import com.example.internalapi.NatsSubject.Reservation.DELETE
import com.example.internalapi.request.reservation.delete.proto.CancelReservationRequest
import com.example.internalapi.request.reservation.delete.proto.CancelReservationResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.service.ReservationService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteReservationNatsController(
    private val reservationService: ReservationService,
) : NatsController<CancelReservationRequest, CancelReservationResponse> {
    override val subject = DELETE
    override val parser: Parser<CancelReservationRequest> = CancelReservationRequest.parser()

    override fun handle(request: CancelReservationRequest): Mono<CancelReservationResponse> {
        return reservationService.cancelReservation(request.userId, request.bookId)
            .map { buildSuccessResponse() }
    }

    private fun buildSuccessResponse(): CancelReservationResponse {
        return CancelReservationResponse.newBuilder().apply { successBuilder }.build()
    }
}
