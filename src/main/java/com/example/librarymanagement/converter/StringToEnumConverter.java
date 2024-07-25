package com.example.librarymanagement.converter;

import com.example.librarymanagement.model.enums.Availability;
import com.example.librarymanagement.model.enums.Genre;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter {
    public static class StringToEnumGenreConverter implements Converter<String, Genre> {

        @Override
        public Genre convert(String source) {
            try {
                return Genre.valueOf(source.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid value for enum " + Genre.class.getSimpleName() + ": " + source, e);
            }
        }
    }

    public static class StringToEnumAvailabilityConverter implements Converter<String, Availability> {

        @Override
        public Availability convert(String source) {
            try {
                return Availability.valueOf(source.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid value for enum " + Availability.class.getSimpleName() + ": " + source, e);
            }
        }
    }
}