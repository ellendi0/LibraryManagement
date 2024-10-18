package com.example.librarymanagement.notification.application.port.`in`

import com.example.librarymanagement.notification.domain.Notification
import reactor.core.publisher.Mono

interface NotifyUserAboutBookAvailabilityInPort {
    fun notifyUserAboutBookAvailability(bookId: String, libraryId: String): Mono<Notification>
}
