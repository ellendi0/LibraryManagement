package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.domain.ReservationOutcome
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.LibraryService
import com.example.librarymanagement.service.ReservationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val bookPresenceService: BookPresenceService,
    private val libraryService: LibraryService,
) : ReservationService {

    override fun getAllReservationsByUserId(userId: String): Flux<Reservation> =
        reservationRepository.findAllByUserId(userId)

    override fun reserveBook(userId: String, libraryId: String, bookId: String): Mono<ReservationOutcome> {
        return validate(userId, libraryId, bookId)
            .flatMap { tryAddUserIfAvailableBook(userId, libraryId, bookId) }
            .switchIfEmpty { saveReservation(userId, libraryId, bookId) }
    }

    override fun cancelReservation(userId: String, bookId: String): Mono<Unit> {
        return reservationRepository.findAllByBookIdAndUserId(bookId, userId)
            .flatMap { reservation ->
                reservation.id?.let { reservationId ->
                    reservationRepository.deleteById(reservationId)
                } ?: Mono.empty()
            }
            .then(Mono.just(Unit))
    }

    override fun deleteReservationById(id: String): Mono<Unit> =
        reservationRepository.findById(id).let { reservationRepository.deleteById(id) }

    private fun tryAddUserIfAvailableBook(userId: String, libraryId: String, bookId: String): Mono<ReservationOutcome> {
        return bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(libraryId, bookId)
            .filter { it.availability == Availability.AVAILABLE }
            .next()
            .flatMap {
                bookPresenceService.addUserToBook(userId, libraryId, bookId)
                    .collectList()
                    .map<ReservationOutcome> { ReservationOutcome.Journals(it) }
            }.switchIfEmpty { Mono.empty() }
    }

    private fun saveReservation(
        userId: String,
        libraryId: String,
        bookId: String
    ): Mono<ReservationOutcome> {
        return reservationRepository.save(Reservation(userId = userId, libraryId = libraryId, bookId = bookId))
            .flatMap {
                getAllReservationsByUserId(userId)
                    .collectList()
                    .map { ReservationOutcome.Reservations(it) }
            }
    }

    private fun validate(userId: String, libraryId: String, bookId: String): Mono<Boolean> {
        return Mono.zip(
            reservationRepository.existsByBookIdAndUserId(bookId, userId),
            libraryService.existsLibraryById(libraryId),
            bookPresenceService.existsBookPresenceByBookIdAndLibraryId(bookId, libraryId)
        )
            .flatMap {
                val existsReservation = it.t1
                val existsBookPresence = it.t3

                when {
                    existsReservation -> Mono.error(ExistingReservationException(bookId, userId))
                    !existsBookPresence -> Mono.error(EntityNotFoundException("Presence of book"))
                    else -> Mono.just(true)
                }
            }
    }

}
