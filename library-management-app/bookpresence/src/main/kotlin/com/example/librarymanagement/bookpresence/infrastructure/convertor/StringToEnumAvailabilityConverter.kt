package com.example.librarymanagement.bookpresence.infrastructure.convertor

import com.example.librarymanagement.bookpresence.domain.Availability
import org.springframework.core.convert.converter.Converter

class StringToEnumAvailabilityConverter : Converter<String, Availability> {
    override fun convert(source: String): Availability {
        return requireNotNull(Availability.valueOf(source.uppercase())) {
            "Invalid value for enum ${Availability::class.simpleName}: $source"
        }
    }
}
