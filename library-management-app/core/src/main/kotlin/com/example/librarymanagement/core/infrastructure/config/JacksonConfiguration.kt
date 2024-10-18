package com.example.librarymanagement.core.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JacksonConfiguration {
    @Bean
    open fun objectMapper(): ObjectMapper {
        val mapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .addModule(JavaTimeModule())
            .build()
        return mapper
    }
}
