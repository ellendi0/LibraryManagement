package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Reservation
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReservationRepository {
    fun save(reservation: Reservation): Mono<Reservation>
    fun findById(reservationId: String): Mono<Reservation>
    fun deleteById(reservationId: String): Mono<Unit>
    fun findAllByBookIdAndUserId(bookId: String, userId: String): Flux<Reservation>
    fun findAllByUserId(userId: String): Flux<Reservation>
    fun findFirstByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Reservation>
    fun existsByBookIdAndUserId(bookId: String, userId: String): Mono<Boolean>
}
