package com.example.librarymanagement.notification.infrastructure.kafka

import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

@Component
class KafkaProducerService(
    @Value("\${spring.kafka.topic}") private val topic: String,
    private val kafkaSender: KafkaSender<String, NotifyOnAvailabilityRequest>
) {
    private val logger = LoggerFactory.getLogger(KafkaProducerService::class.java)

    fun sendNotification(bookId: String, libraryId: String): Mono<Void> {
        val notifyRequest = NotifyOnAvailabilityRequest.newBuilder()
            .setBookId(bookId)
            .setLibraryId(libraryId)
            .build()

        logger.info("Sending message to Kafka - Topic: $topic, BookId: $bookId, LibraryId: $libraryId")

        val senderRecord = SenderRecord.create(ProducerRecord(topic, bookId, notifyRequest), bookId)

        return kafkaSender.send(Flux.just(senderRecord))
            .doOnError { logger.error("Failed to send message for BookId: $bookId, LibraryId: $libraryId", it) }
            .doOnNext { logger.info("Message sent successfully for BookId: $bookId, LibraryId: $libraryId") }
            .then(Mono.empty())
    }
}
