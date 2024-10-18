package com.example.librarymanagement.reservation.application.port.`in`

import com.example.librarymanagement.reservation.domain.Reservation
import reactor.core.publisher.Flux

interface FindAllReservationByUserIdInPort {
    fun findAllReservationsByUserId(userId: String): Flux<Reservation>
}
