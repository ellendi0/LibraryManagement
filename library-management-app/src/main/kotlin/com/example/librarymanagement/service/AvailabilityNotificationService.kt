package com.example.librarymanagement.service

import reactor.core.publisher.Mono

interface AvailabilityNotificationService {
    fun notifyUserAboutBookAvailability(bookId: String, libraryId: String): Mono<Unit>
}
