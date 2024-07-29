package com.example.librarymanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genre {
    POETRY, PROSE, DRAMA;

    @JsonCreator
    public static Genre fromValue(String value) {return Genre.valueOf(value.toUpperCase());}
}