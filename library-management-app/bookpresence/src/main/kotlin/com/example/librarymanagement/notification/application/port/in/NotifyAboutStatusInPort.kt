package com.example.librarymanagement.notification.application.port.`in`

import com.example.librarymanagement.notification.domain.Notification
import reactor.core.publisher.Mono

interface NotifyAboutStatusInPort {
    fun notifyAboutStatus(bookId: String, libraryId: String): Mono<Notification>
}
