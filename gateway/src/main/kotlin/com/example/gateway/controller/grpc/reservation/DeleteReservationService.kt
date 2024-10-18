package com.example.com.example.gateway.controller.grpc.reservation

import com.example.gateway.publisher.reservation.DeleteReservationNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorDeleteReservationServiceGrpc
import com.example.internalapi.request.reservation.delete.proto.CancelReservationRequest
import com.example.internalapi.request.reservation.delete.proto.CancelReservationResponse
import reactor.core.publisher.Mono

@GrpcService
class DeleteReservationService(
    private val deleteReservationNatsReservation: DeleteReservationNatsPublisher
) : ReactorDeleteReservationServiceGrpc.DeleteReservationServiceImplBase() {

    override fun deleteReservation(request: Mono<CancelReservationRequest>): Mono<CancelReservationResponse> =
        request.flatMap { reservation -> deleteReservationNatsReservation.request(reservation) }
}
