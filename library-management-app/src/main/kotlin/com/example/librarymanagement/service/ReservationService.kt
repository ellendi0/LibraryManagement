package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.ReservationOutcome
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReservationService {
    fun getAllReservationsByUserId(userId: String): Flux<Reservation>
    fun reserveBook(userId: String, libraryId: String, bookId: String): Mono<ReservationOutcome>
    fun cancelReservation(userId: String, bookId: String): Mono<Unit>
    fun deleteReservationById(id: String): Mono<Unit>
}
