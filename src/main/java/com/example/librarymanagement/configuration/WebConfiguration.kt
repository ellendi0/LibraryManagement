package com.example.librarymanagement.configuration;

import com.example.librarymanagement.converter.StringToEnumAvailabilityConverter;
import com.example.librarymanagement.converter.StringToEnumGenreConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumGenreConverter());
        registry.addConverter(new StringToEnumAvailabilityConverter());
    }
}