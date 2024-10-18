package com.example.librarymanagement.reservation.application.service

import com.example.librarymanagement.reservation.application.port.`in`.FindAllReservationByUserIdInPort
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import com.example.librarymanagement.reservation.domain.Reservation
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class FindAllReservationByUserIdUseCase(
    private val reservationRepository: ReservationRepositoryOutPort
) : FindAllReservationByUserIdInPort {
    override fun findAllReservationsByUserId(userId: String): Flux<Reservation> =
        reservationRepository.findAllByUserId(userId)
}
