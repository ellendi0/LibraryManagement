package com.example.librarymanagement.converter;

import com.example.librarymanagement.model.enums.Availability;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumAvailabilityConverter implements Converter<String, Availability> {

    @Override
    public Availability convert(String source) {
        if(source != null) {
            return Availability.valueOf(source.trim().toUpperCase());
        } else
            throw new IllegalArgumentException("Invalid value for enum " + Availability.class.getSimpleName() + ": " + source);
        }
}