package com.example.librarymanagement.converter;

import com.example.librarymanagement.model.enums.Genre;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumGenreConverter implements Converter<String, Genre> {

    @Override
    public Genre convert(String source) {
        if(source != null) {
            return Genre.valueOf(source.trim().toUpperCase());
        } else {
            throw new IllegalArgumentException("Invalid value for enum " + Genre.class.getSimpleName() + ": " + source);
        }
    }
}