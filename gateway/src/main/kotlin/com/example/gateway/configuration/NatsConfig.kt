package com.example.gateway.configuration

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException

@Configuration
class NatsConfig {
    @Value("\${nats.url}")
    private val natsUrl: String? = null

    @Bean
    @Throws(IOException::class, InterruptedException::class)
    fun natsConnection(): Connection {
        return Nats.connect(natsUrl)
    }
}
