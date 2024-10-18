package com.example.librarymanagement.reservation.application.port.`in`

import reactor.core.publisher.Mono

interface DeleteReservationByIdInPort {
    fun deleteReservationById(id: String): Mono<Unit>
}
