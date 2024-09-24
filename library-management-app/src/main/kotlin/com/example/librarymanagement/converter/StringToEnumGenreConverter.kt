package com.example.librarymanagement.converter

import com.example.librarymanagement.model.enums.Genre
import org.springframework.core.convert.converter.Converter

class StringToEnumGenreConverter : Converter<String, Genre> {
    override fun convert(source: String): Genre {
        return requireNotNull(Genre.valueOf(source.uppercase())) {
            "Invalid value for enum ${Genre::class.simpleName}: $source"
        }
    }
}
