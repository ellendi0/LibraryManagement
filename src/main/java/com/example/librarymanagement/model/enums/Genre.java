package com.example.librarymanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genre {
    POETRY("Poetry"),
    PROSE("Prose"),
    DRAMA("Drama");

    private String value;

    Genre(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Genre fromString(String value) {
        for (Genre genre : Genre.values()) {
            if (genre.value.equalsIgnoreCase(value)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("There is no genre with value " + value + ".\nMust be one of Poetry, Prose, Drama");
    }
}