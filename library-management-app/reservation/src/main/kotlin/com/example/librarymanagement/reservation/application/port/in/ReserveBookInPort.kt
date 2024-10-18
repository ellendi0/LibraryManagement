package com.example.librarymanagement.reservation.application.port.`in`

import com.example.librarymanagement.reservation.domain.Reservation
import reactor.core.publisher.Mono

interface ReserveBookInPort {
    fun reserveBook(userId: String, libraryId: String, bookId: String): Mono<Reservation>
}
