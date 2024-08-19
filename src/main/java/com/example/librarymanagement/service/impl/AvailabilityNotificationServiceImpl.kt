package com.example.librarymanagement.service.impl

import com.example.librarymanagement.repository.ReservationRepository
import com.example.librarymanagement.service.AvailabilityNotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AvailabilityNotificationServiceImpl(
    private val reservationRepository: ReservationRepository
): AvailabilityNotificationService {

    private val log: Logger = LoggerFactory.getLogger(AvailabilityNotificationServiceImpl::class.java)

    override fun notifyUserAboutBookAvailability(bookId: String, libraryId: String?) {
        val reservation = reservationRepository.findFirstByBookIdAndLibraryIdOrLibraryIsNull(bookId, libraryId)
        reservation?.let {
            log.info("Book with id $bookId is available in library with id $libraryId. " +
                "User with id ${it.user.id} can borrow it.")}
    }
}
