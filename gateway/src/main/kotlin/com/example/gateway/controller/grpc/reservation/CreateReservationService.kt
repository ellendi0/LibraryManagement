package com.example.com.example.gateway.controller.grpc.reservation

import com.example.gateway.publisher.reservation.CreateReservationNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorCreateReservationServiceGrpc
import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.internalapi.request.reservation.create.proto.CreateReservationResponse
import reactor.core.publisher.Mono

@GrpcService
class CreateReservationService(
    private val createReservationNatsReservation: CreateReservationNatsPublisher,
) : ReactorCreateReservationServiceGrpc.CreateReservationServiceImplBase() {

    override fun createReservation(request: Mono<CreateReservationRequest>): Mono<CreateReservationResponse> =
        request.flatMap { reservation -> createReservationNatsReservation.request(reservation) }
}
