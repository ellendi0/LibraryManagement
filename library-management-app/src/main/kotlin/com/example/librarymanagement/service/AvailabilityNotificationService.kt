package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Notification
import reactor.core.publisher.Mono

interface AvailabilityNotificationService {
    fun notifyUserAboutBookAvailability(bookId: String, libraryId: String): Mono<Notification>
}
