package com.example.librarymanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Availability {
    AVAILABLE, UNAVAILABLE;

    @JsonCreator
    public static Availability fromValue(String value) {
        return Availability.valueOf(value.toUpperCase());
    }
}