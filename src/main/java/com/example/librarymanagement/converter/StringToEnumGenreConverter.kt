package com.example.librarymanagement.converter

import com.example.librarymanagement.model.enums.Genre
import org.springframework.core.convert.converter.Converter

class StringToEnumGenreConverter : Converter<String, Genre> {
    override fun convert(source: String): Genre {
        if (source.isNotBlank()) {
            return Genre.valueOf(source.uppercase())
        } else {
            throw IllegalArgumentException("Invalid value for enum ${Genre::class.simpleName}: $source")
        }
    }
}
