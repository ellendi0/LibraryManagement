package com.example.librarymanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Availability {
    AVAILABLE("Available"), NOT_AVAILABLE("Unavailable");

    private String value;

    Availability(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Availability fromString(String value) {
        for (Availability availability: Availability.values()) {
            if (availability.value.equalsIgnoreCase(value)) {
                return availability;
            }
        }
        throw new IllegalArgumentException("No availability with value " + value + ".\nMust be one of available, unavailable");
    }
}