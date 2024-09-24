package com.example.librarymanagement.converter

import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.enums.Genre
import org.springframework.core.convert.converter.Converter

class StringToEnumAvailabilityConverter : Converter<String, Availability> {
    override fun convert(source: String): Availability {
        return requireNotNull(Availability.valueOf(source.uppercase())) {
            "Invalid value for enum ${Genre::class.simpleName}: $source"
        }
    }
}
