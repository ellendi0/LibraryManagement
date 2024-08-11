package com.example.librarymanagement.configuration

import com.example.librarymanagement.converter.StringToEnumAvailabilityConverter
import com.example.librarymanagement.converter.StringToEnumGenreConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToEnumGenreConverter())
        registry.addConverter(StringToEnumAvailabilityConverter())
    }
}
