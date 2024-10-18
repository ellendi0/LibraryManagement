package com.example.librarymanagement.controller.nats.reservation

import com.example.internalapi.NatsSubject.Reservation.GET_ALL_BY_USER_ID
import com.example.internalapi.model.Reservation
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.ReservationMapper
import com.example.librarymanagement.service.ReservationService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllReservationByUserIdNatsController(
    private val reservationService: ReservationService,
    private val reservationMapper: ReservationMapper,
) : NatsController<GetAllReservationsByUserIdRequest, GetAllReservationsByUserIdResponse> {
    override val subject = GET_ALL_BY_USER_ID
    override val parser: Parser<GetAllReservationsByUserIdRequest> = GetAllReservationsByUserIdRequest.parser()

    override fun handle(request: GetAllReservationsByUserIdRequest): Mono<GetAllReservationsByUserIdResponse> {
        return reservationService.getAllReservationsByUserId(request.userId)
            .map { reservationMapper.toReservationProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(reservations: List<Reservation>): GetAllReservationsByUserIdResponse {
        return GetAllReservationsByUserIdResponse.newBuilder()
            .apply { successBuilder.reservationsBuilder.addAllReservations(reservations) }
            .build()
    }
}
