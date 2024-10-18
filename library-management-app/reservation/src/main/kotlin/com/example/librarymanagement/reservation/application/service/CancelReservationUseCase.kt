package com.example.librarymanagement.reservation.application.service

import com.example.librarymanagement.reservation.application.port.`in`.CancelReservationInPort
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CancelReservationUseCase(
    private val reservationRepository: ReservationRepositoryOutPort
): CancelReservationInPort {
    override fun cancelReservation(userId: String, bookId: String): Mono<Unit> {
        return reservationRepository.findAllByBookIdAndUserId(bookId, userId)
            .flatMap { reservation ->
                reservation.id?.let { reservationId ->
                    reservationRepository.deleteById(reservationId)
                } ?: Mono.empty()
            }
            .then(Mono.just(Unit))
    }}
