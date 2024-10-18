package com.example.librarymanagement.service.impl

import com.example.librarymanagement.model.domain.Notification
import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.AvailabilityNotificationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AvailabilityNotificationServiceImpl(
    private val reservationRepository: ReservationRepository
) : AvailabilityNotificationService {

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
