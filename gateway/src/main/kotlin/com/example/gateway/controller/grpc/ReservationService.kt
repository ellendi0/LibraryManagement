package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.subcriber.NotifyOnAvailabilityNatsSubscriber
import com.example.gateway.publisher.reservation.CreateReservationNatsPublisher
import com.example.gateway.publisher.reservation.DeleteReservationNatsPublisher
import com.example.gateway.publisher.reservation.GetAllReservationByUserIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorReservationServiceGrpc
import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.internalapi.request.reservation.create.proto.CreateReservationResponse
import com.example.internalapi.request.reservation.delete.proto.CancelReservationRequest
import com.example.internalapi.request.reservation.delete.proto.CancelReservationResponse
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdResponse
import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@GrpcService
class ReservationService(
    private val createReservationNatsReservation: CreateReservationNatsPublisher,
    private val deleteReservationNatsReservation: DeleteReservationNatsPublisher,
    private val getAllReservationByUserIdNatsPublisher: GetAllReservationByUserIdNatsPublisher,
    private val notifyOnAvailabilityNatsSubscriber: NotifyOnAvailabilityNatsSubscriber
) : ReactorReservationServiceGrpc.ReservationServiceImplBase() {

    override fun createReservation(request: Mono<CreateReservationRequest>): Mono<CreateReservationResponse> =
        request.flatMap { reservation -> createReservationNatsReservation.request(reservation) }

    override fun getAllReservationByUserId(
        request: Mono<GetAllReservationsByUserIdRequest>
    ): Mono<GetAllReservationsByUserIdResponse> =
        request.flatMap { reservation -> getAllReservationByUserIdNatsPublisher.request(reservation) }

    override fun deleteReservation(request: Mono<CancelReservationRequest>): Mono<CancelReservationResponse> =
        request.flatMap { reservation -> deleteReservationNatsReservation.request(reservation) }

    override fun notifyOnAvailability(request: Mono<NotifyOnAvailabilityRequest>): Flux<NotifyOnAvailabilityResponse> {
        return request.flatMapMany { notifyOnAvailabilityNatsSubscriber.subscribe(request) }
    }
}
