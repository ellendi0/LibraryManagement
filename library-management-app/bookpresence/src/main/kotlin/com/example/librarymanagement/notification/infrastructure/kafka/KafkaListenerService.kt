package com.example.librarymanagement.notification.infrastructure.kafka

import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import com.example.librarymanagement.notification.application.port.`in`.NotifyAboutStatusInPort
import com.example.librarymanagement.notification.infrastructure.nats.NotifyOnAvailabilityNatsPublisher
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver

@Component
class KafkaListenerService(
    private val kafkaReceiver: KafkaReceiver<String, NotifyOnAvailabilityRequest>,
    private val notifyOnAvailabilityNatsPublisher: NotifyOnAvailabilityNatsPublisher,
    private val notifyAboutStatusInPort: NotifyAboutStatusInPort
) {
    private val logger = LoggerFactory.getLogger(KafkaListenerService::class.java)

    @PostConstruct
    fun init() {
        receiveNotifications().subscribe()
    }

    private fun receiveNotifications(): Mono<Void> =
        kafkaReceiver.receive()
            .flatMap { record ->
                val notification = record.value()
                logger.info(
                    "Received message from Kafka - BookId: ${notification.bookId}, LibraryId: ${notification.libraryId}"
                )
                record.receiverOffset().acknowledge()

                notifyAboutStatusInPort.notifyAboutStatus(notification.bookId, notification.libraryId)
                    .flatMap { notificationStatus ->
                        notifyOnAvailabilityNatsPublisher.publish(
                            notification.bookId,
                            notification.libraryId,
                            notificationStatus
                        )
                    }
            }
            .doOnError { e ->
                logger.error("Error while processing Kafka message", e)
            }
            .repeat()
            .then()
}
