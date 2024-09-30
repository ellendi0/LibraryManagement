package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.exception.BookAvailabilityException
import com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.publisher.reservation.CreateReservationNatsPublisher
import com.example.gateway.publisher.reservation.DeleteReservationNatsPublisher
import com.example.gateway.publisher.reservation.GetAllReservationByUserIdNatsPublisher
import com.example.internalapi.ReactorReservationServiceGrpc
import com.example.internalapi.request.reservation.create.proto.CreateReservationRequest
import com.example.internalapi.request.reservation.create.proto.CreateReservationResponse
import com.example.internalapi.request.reservation.create.proto.CreateReservationResponse.Failure
import com.example.internalapi.request.reservation.delete.proto.CancelReservationRequest
import com.example.internalapi.request.reservation.delete.proto.CancelReservationResponse
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdRequest
import com.example.internalapi.request.reservation.get_all_by_user_id.proto.GetAllReservationsByUserIdResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class ReservationService(
    private val createReservationNatsReservation: CreateReservationNatsPublisher,
    private val deleteReservationNatsReservation: DeleteReservationNatsPublisher,
    private val getAllReservationByUserIdNatsPublisher: GetAllReservationByUserIdNatsPublisher
) : ReactorReservationServiceGrpc.ReservationServiceImplBase() {

    override fun createReservation(request: Mono<CreateReservationRequest>): Mono<CreateReservationResponse> {
        return request
            .flatMap { reservation -> createReservationNatsReservation.request(reservation) }
            .flatMap {
                if (it.hasSuccess()) handleCreationSuccess(it) else Mono.error(handleCreationFailure(it.failure))
            }
    }

    override fun getAllReservationByUserId(
        request: Mono<GetAllReservationsByUserIdRequest>
    ): Mono<GetAllReservationsByUserIdResponse> {
        return request
            .flatMap { reservation -> getAllReservationByUserIdNatsPublisher.request(reservation) }
            .flatMap { handleGetAllReservationSuccess(it) }
    }

    override fun deleteReservation(request: Mono<CancelReservationRequest>): Mono<CancelReservationResponse> {
        return request.flatMap { reservation -> deleteReservationNatsReservation.request(reservation) }
    }

    private fun handleCreationSuccess(response: CreateReservationResponse): Mono<CreateReservationResponse> {
        return CreateReservationResponse.newBuilder().setSuccess(response.success).build().toMono()
    }

    private fun handleCreationFailure(failure: Failure): Exception {
        return when (failure.errorCase) {
            Failure.ErrorCase.ERROR_NOT_SET -> Exception()
            Failure.ErrorCase.UNKNOWN_ERROR -> Exception(failure.unknownError.messages)
            Failure.ErrorCase.NOT_FOUND_ERROR -> EntityNotFoundException(failure.notFoundError.messages)
            Failure.ErrorCase.BOOK_AVAILABILITY_ERROR ->
                BookAvailabilityException(failure.bookAvailabilityError.messages)

            null -> IllegalStateException("Error case is null")
        }
    }

    private fun handleGetAllReservationSuccess(
        response: GetAllReservationsByUserIdResponse
    ): Mono<GetAllReservationsByUserIdResponse> {
        return GetAllReservationsByUserIdResponse.newBuilder().setSuccess(response.success).build().toMono()
    }
}
