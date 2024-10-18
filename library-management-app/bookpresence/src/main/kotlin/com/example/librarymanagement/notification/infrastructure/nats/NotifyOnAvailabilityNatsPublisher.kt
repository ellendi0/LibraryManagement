package com.example.librarymanagement.notification.infrastructure.nats

import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityResponse
import com.example.librarymanagement.notification.domain.Notification
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import com.example.internalapi.model.Notification as ProtoNotification

@Component
class NotifyOnAvailabilityNatsPublisher(val connection: Connection) {
    fun publish(bookId: String, libraryId: String, notification: Notification): Mono<Unit> =
        connection.publish(
            "reservation.${bookId}.${libraryId}", NotifyOnAvailabilityResponse.newBuilder()
                .apply {
                    setNotification(
                        ProtoNotification.newBuilder()
                            .apply { setMessage(notification.message) }.build()
                    )
                }.build().toByteArray()
        ).toMono()
}
