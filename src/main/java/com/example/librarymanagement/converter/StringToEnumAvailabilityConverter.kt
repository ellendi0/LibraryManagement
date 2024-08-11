package com.example.librarymanagement.converter

import com.example.librarymanagement.model.enums.Availability
import org.springframework.core.convert.converter.Converter

class StringToEnumAvailabilityConverter : Converter<String, Availability> {
    override fun convert(source: String): Availability {
        if (source.isNotBlank()) {
            return Availability.valueOf(source.uppercase())
        } else {
            throw IllegalArgumentException("Invalid value for enum ${Availability::class.simpleName}: $source")
        }
    }
}
