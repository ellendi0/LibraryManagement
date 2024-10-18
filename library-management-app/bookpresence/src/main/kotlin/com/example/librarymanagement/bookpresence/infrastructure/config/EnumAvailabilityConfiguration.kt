package com.example.librarymanagement.bookpresence.infrastructure.config

import com.example.librarymanagement.bookpresence.domain.Availability
import com.example.librarymanagement.bookpresence.infrastructure.convertor.StringToEnumAvailabilityConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

@Configuration
open class EnumAvailabilityConfiguration {
    @Bean
    open fun stringToEnumAvailabilityConverter(): Converter<String, Availability> {
        return StringToEnumAvailabilityConverter()
    }
}
