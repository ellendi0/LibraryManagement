package com.example.librarymanagement.service.impl

import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.AvailabilityNotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AvailabilityNotificationServiceImpl(
    private val reservationRepository: ReservationRepository
) : AvailabilityNotificationService {

    private val log: Logger = LoggerFactory.getLogger(AvailabilityNotificationServiceImpl::class.java)

    override fun notifyUserAboutBookAvailability(bookId: String, libraryId: String): Mono<Unit> {
        return reservationRepository.findFirstByBookIdAndLibraryId(bookId, libraryId)
            .doOnNext { reservation ->
                log.info(
                    "Book with id $bookId is available in library with id $libraryId. " +
                            "User with id ${reservation.userId} can borrow it."
                )
            }
            .then(Mono.just(Unit))
    }
}
