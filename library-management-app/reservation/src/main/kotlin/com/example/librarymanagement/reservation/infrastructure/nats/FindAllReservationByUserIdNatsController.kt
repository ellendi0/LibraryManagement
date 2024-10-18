package com.example.librarymanagement.reservation.infrastructure.nats

import com.example.internalapi.NatsSubject.Reservation.FIND_ALL_BY_USER_ID
import com.example.internalapi.model.Reservation
import com.example.internalapi.request.reservation.find_all_by_user_id.proto.FindAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.find_all_by_user_id.proto.FindAllReservationsByUserIdResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.reservation.application.port.`in`.FindAllReservationByUserIdInPort
import com.example.librarymanagement.reservation.infrastructure.convertor.ReservationMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllReservationByUserIdNatsController(
    private val findAllReservationByUserIdInPort: FindAllReservationByUserIdInPort,
    private val reservationMapper: ReservationMapper,
) : NatsController<FindAllReservationsByUserIdRequest, FindAllReservationsByUserIdResponse> {
    override val subject = FIND_ALL_BY_USER_ID
    override val parser: Parser<FindAllReservationsByUserIdRequest> = FindAllReservationsByUserIdRequest.parser()

    override fun handle(request: FindAllReservationsByUserIdRequest): Mono<FindAllReservationsByUserIdResponse> {
        return findAllReservationByUserIdInPort.findAllReservationsByUserId(request.userId)
            .map { reservationMapper.toReservationProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(reservations: List<Reservation>): FindAllReservationsByUserIdResponse {
        return FindAllReservationsByUserIdResponse.newBuilder()
            .apply { successBuilder.reservationsBuilder.addAllReservations(reservations) }
            .build()
    }
}
