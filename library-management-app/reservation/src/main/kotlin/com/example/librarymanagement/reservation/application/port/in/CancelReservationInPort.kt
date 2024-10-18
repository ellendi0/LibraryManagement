package com.example.librarymanagement.reservation.application.port.`in`

import reactor.core.publisher.Mono

interface CancelReservationInPort {
    fun cancelReservation(userId: String, bookId: String): Mono<Unit>
}
