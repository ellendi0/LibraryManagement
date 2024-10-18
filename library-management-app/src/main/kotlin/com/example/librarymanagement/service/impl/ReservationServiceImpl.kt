package com.example.librarymanagement.service.impl

import com.example.librarymanagement.exception.BookAvailabilityException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.ExistingReservationException
import com.example.librarymanagement.model.domain.Reservation
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.BookPresenceService
import com.example.librarymanagement.service.LibraryService
import com.example.librarymanagement.service.ReservationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val bookPresenceService: BookPresenceService,
    private val libraryService: LibraryService,
) : ReservationService {

    override fun getAllReservationsByUserId(userId: String): Flux<Reservation> =
        reservationRepository.findAllByUserId(userId)

    override fun reserveBook(userId: String, libraryId: String, bookId: String): Mono<Reservation> {
        return validate(userId, libraryId, bookId)
            .flatMap { saveReservation(userId, libraryId, bookId) }
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

    private fun saveReservation(
        userId: String,
        libraryId: String,
        bookId: String
    ): Mono<Reservation> =
        reservationRepository.save(Reservation(userId = userId, libraryId = libraryId, bookId = bookId))

    private fun validate(userId: String, libraryId: String, bookId: String): Mono<Boolean> {
        return Mono.zip(
            reservationRepository.existsByBookIdAndUserId(bookId, userId),
            libraryService.existsLibraryById(libraryId),
            bookPresenceService.existsBookPresenceByBookIdAndLibraryId(bookId, libraryId),
            bookPresenceService.existsAvailableBookInLibrary(bookId, libraryId)
        )
            .flatMap {
                val existsReservation = it.t1
                val existsBookPresence = it.t3
                val existsAvailableBookPresence = it.t4

                when {
                    existsReservation -> Mono.error(ExistingReservationException(bookId, userId))
                    !existsBookPresence -> Mono.error(EntityNotFoundException("Presence of book"))
                    existsAvailableBookPresence -> Mono.error(
                        BookAvailabilityException(
                            libraryId,
                            bookId,
                            Availability.AVAILABLE
                        )
                    )

                    else -> Mono.just(true)
                }
            }
    }
}
