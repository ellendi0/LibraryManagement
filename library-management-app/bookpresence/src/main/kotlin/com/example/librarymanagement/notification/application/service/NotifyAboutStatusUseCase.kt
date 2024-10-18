package com.example.librarymanagement.notification.application.service

import com.example.librarymanagement.notification.application.port.`in`.NotifyAboutStatusInPort
import com.example.librarymanagement.notification.domain.Notification
import com.example.librarymanagement.reservation.application.port.out.ReservationRepositoryOutPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NotifyAboutStatusUseCase(
    private val reservationRepository: ReservationRepositoryOutPort
): NotifyAboutStatusInPort{
    override fun notifyAboutStatus(bookId: String, libraryId: String): Mono<Notification> {
        return reservationRepository.findFirstByBookIdAndLibraryId(bookId, libraryId)
            .map {
                Notification(
                    "Book with id $bookId was borrowed in library with id $libraryId. " +
                            "User with id ${it.userId} can borrow it after returning."
                )
            }
    }
}
