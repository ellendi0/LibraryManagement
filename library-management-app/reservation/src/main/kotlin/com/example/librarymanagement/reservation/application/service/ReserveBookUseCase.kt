package com.example.librarymanagement.reservation.application.service

import com.example.librarymanagement.core.application.exception.ExistingReservationException
import com.example.librarymanagement.library.application.port.`in`.ExistsLibraryWithAvailableBookInPort
import com.example.librarymanagement.reservation.application.port.`in`.ReserveBookInPort
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import com.example.librarymanagement.reservation.domain.Reservation
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.example.librarymanagement.user.application.port.`in`.ExistsUserByIdInPort

@Service
class ReserveBookUseCase(
    private val reservationRepository: ReservationRepositoryOutPort,
    private val existsLibraryWithAvailableBookInPort: ExistsLibraryWithAvailableBookInPort,
    private val existsUserByIdInPort: ExistsUserByIdInPort
) : ReserveBookInPort {
    override fun reserveBook(userId: String, libraryId: String, bookId: String): Mono<Reservation> {
        return validate(userId, libraryId, bookId)
            .flatMap { reservationRepository.save(Reservation(null, userId, bookId, libraryId)) }
    }

    private fun validate(userId: String, libraryId: String, bookId: String): Mono<Boolean> {
        return Mono.zip(
            reservationRepository.existsByBookIdAndUserId(bookId, userId),
            existsLibraryWithAvailableBookInPort.existsLibraryWithAvailableBook(bookId, libraryId),
            existsUserByIdInPort.existsUserById(userId)
        )
            .flatMap {
                val existsReservation = it.t1

                when {
                    existsReservation -> Mono.error(ExistingReservationException(bookId, userId))
                    else -> Mono.just(true)
                }
            }
    }
}
