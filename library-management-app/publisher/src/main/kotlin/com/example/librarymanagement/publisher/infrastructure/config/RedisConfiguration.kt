package com.example.librarymanagement.publisher.infrastructure.config

import com.example.librarymanagement.publisher.domain.Publisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder

@Configuration
class RedisConfiguration {
    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Publisher> {
        val serializer: Jackson2JsonRedisSerializer<Publisher> = Jackson2JsonRedisSerializer(Publisher::class.java)
        val builder: RedisSerializationContextBuilder<String, Publisher> =
            RedisSerializationContext.newSerializationContext(Jackson2JsonRedisSerializer(String::class.java))
        val context: RedisSerializationContext<String, Publisher> = builder.value(serializer).build()
        return ReactiveRedisTemplate(factory, context)
    }
}
