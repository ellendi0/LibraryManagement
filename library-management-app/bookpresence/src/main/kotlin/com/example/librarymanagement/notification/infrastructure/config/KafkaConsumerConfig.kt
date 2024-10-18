package com.example.librarymanagement.notification.infrastructure.config

import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import com.google.protobuf.GeneratedMessageV3
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
open class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.consumer.group-id}") private val groupId: String,
    @Value("\${spring.kafka.topic}") private val topicName: String,
    @Value("\${spring.kafka.properties.schema.registry.url}") private val schemaRegistryUrl: String
) {
    @Bean
    open fun kafkaReceiver(): KafkaReceiver<String, NotifyOnAvailabilityRequest> = createKafkaReceiver(consumerProperties())

    private fun <T : GeneratedMessageV3> createKafkaReceiver(
        properties: MutableMap<String, Any>
    ): KafkaReceiver<String, T> {
        val receiverOptions = ReceiverOptions.create<String, T>(properties)
        return KafkaReceiver.create(receiverOptions.subscription(listOf(topicName)))
    }

    private fun consumerProperties(): MutableMap<String, Any> = mutableMapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        ConsumerConfig.GROUP_ID_CONFIG to groupId,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaProtobufDeserializer::class.java,
        KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE to NotifyOnAvailabilityRequest::class.java.name,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        "schema.registry.url" to schemaRegistryUrl

    )
}
