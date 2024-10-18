package com.example.librarymanagement.notification.application.service

import com.example.librarymanagement.notification.application.port.`in`.NotifyUserAboutBookAvailabilityInPort
import com.example.librarymanagement.notification.domain.Notification
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NotifyUserAboutBookAvailabilityUseCase(
    private val reservationRepository: ReservationRepositoryOutPort
) : NotifyUserAboutBookAvailabilityInPort {
    override fun notifyUserAboutBookAvailability(bookId: String, libraryId: String): Mono<Notification> {
        return reservationRepository.findFirstByBookIdAndLibraryId(bookId, libraryId)
            .map {
                Notification(
                    "Book with id $bookId is available in library with id $libraryId. " +
                            "User with id ${it.userId} can borrow it."
                )
            }
    }
}
