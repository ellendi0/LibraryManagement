package com.example.librarymanagement.reservation.application.service

import com.example.librarymanagement.reservation.application.port.`in`.DeleteReservationByIdInPort
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeleteReservationByIdUseCase(
    private val reservationRepository: ReservationRepositoryOutPort
) : DeleteReservationByIdInPort {
    override fun deleteReservationById(id: String): Mono<Unit> =
        reservationRepository.findById(id).let { reservationRepository.deleteById(id) }
}
