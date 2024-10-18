package com.example.librarymanagement.notification.infrastructure.config

import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import com.google.protobuf.GeneratedMessageV3
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions

@Configuration
open class KafkaProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.properties.schema.registry.url}") private val schemaRegistryUrl: String
) {

    @Bean
    open fun kafkaSender(): KafkaSender<String, NotifyOnAvailabilityRequest> = createKafkaSender(producerProperties())

    private fun <T : GeneratedMessageV3> createKafkaSender(
        properties: MutableMap<String, Any>
    ): KafkaSender<String, T> =
        KafkaSender.create(SenderOptions.create(properties))

    private fun producerProperties(): MutableMap<String, Any> = mutableMapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaProtobufSerializer::class.java,
        "schema.registry.url" to schemaRegistryUrl
    )
}
