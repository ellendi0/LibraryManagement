package com.example.com.example.gateway.controller.grpc.reservation

import com.example.gateway.publisher.reservation.FindAllReservationsByUserIdNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorFindAllReservationsByUserIdServiceGrpc
import com.example.internalapi.request.reservation.find_all_by_user_id.proto.FindAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.find_all_by_user_id.proto.FindAllReservationsByUserIdResponse
import reactor.core.publisher.Mono

@GrpcService
class FindAllReservationByUserIdService(
    private val findAllReservationsByUserIdNatsPublisher: FindAllReservationsByUserIdNatsPublisher
) : ReactorFindAllReservationsByUserIdServiceGrpc.FindAllReservationsByUserIdServiceImplBase() {

    override fun findAllReservationByUserId(
        request: Mono<FindAllReservationsByUserIdRequest>
    ): Mono<FindAllReservationsByUserIdResponse> =
        request.flatMap { reservation -> findAllReservationsByUserIdNatsPublisher.request(reservation) }
}
