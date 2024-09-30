package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Reservation
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReservationService {
    fun getAllReservationsByUserId(userId: String): Flux<Reservation>
    fun reserveBook(userId: String, libraryId: String, bookId: String): Mono<Reservation>
    fun cancelReservation(userId: String, bookId: String): Mono<Unit>
    fun deleteReservationById(id: String): Mono<Unit>
}
