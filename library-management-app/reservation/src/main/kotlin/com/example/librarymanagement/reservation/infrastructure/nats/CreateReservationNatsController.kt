package com.example.librarymanagement.reservation.infrastructure.nats

import com.example.internalapi.NatsSubject.Reservation.CREATE
import com.example.internalapi.model.Reservation
import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.internalapi.request.reservation.create.proto.CreateReservationResponse
import com.example.librarymanagement.core.application.exception.BookAvailabilityException
import com.example.librarymanagement.core.application.exception.DuplicateKeyException
import com.example.librarymanagement.core.application.exception.EntityNotFoundException
import com.example.librarymanagement.core.infrastructure.convertor.ErrorMapper
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.reservation.application.port.`in`.ReserveBookInPort
import com.example.librarymanagement.reservation.infrastructure.convertor.ReservationMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateReservationNatsController(
    private val reserveBookInPort: ReserveBookInPort,
    private val reservationMapper: ReservationMapper,
    private val errorMapper: ErrorMapper
) : NatsController<CreateReservationRequest, CreateReservationResponse> {
    override val subject = CREATE
    override val parser: Parser<CreateReservationRequest> = CreateReservationRequest.parser()

    override fun handle(request: CreateReservationRequest): Mono<CreateReservationResponse> {
        return reservationMapper.toReservation(request)
            .let { reserveBookInPort.reserveBook(request.userId, request.libraryId, request.bookId) }
            .map { reservationMapper.toReservationProto(it) }
            .map { buildSuccessResponse(it) }
            .onErrorResume { exception -> Mono.just(buildFailureResponse(exception)) }
    }

    private fun buildSuccessResponse(reservation: Reservation): CreateReservationResponse {
        return CreateReservationResponse.newBuilder().apply { successBuilder.setReservation(reservation) }
            .build()
    }

    private fun buildFailureResponse(exception: Throwable): CreateReservationResponse {
        return CreateReservationResponse.newBuilder().apply {
            val error = errorMapper.toErrorProto(exception)
            when (exception) {
                is EntityNotFoundException -> failureBuilder.setNotFoundError(error)
                is BookAvailabilityException -> failureBuilder.setBookAvailabilityError(error)
                is DuplicateKeyException -> failureBuilder.setExistingReservationError(error)
                is Exception -> failureBuilder.setUnknownError(error)
            }
        }.build()
    }
}
