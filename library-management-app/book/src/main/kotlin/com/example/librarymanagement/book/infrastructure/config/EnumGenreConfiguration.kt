package com.example.librarymanagement.book.infrastructure.config

import com.example.librarymanagement.book.domain.Genre
import com.example.librarymanagement.book.infrastructure.convertor.StringToEnumGenreConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

@Configuration
open class EnumGenreConfiguration {
    @Bean
    open fun stringToEnumGenreConverter(): Converter<String, Genre> {
        return StringToEnumGenreConverter()
    }
}
