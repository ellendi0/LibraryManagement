package com.example.librarymanagement.model.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class Availability {
    AVAILABLE, UNAVAILABLE;

    @JsonCreator
    fun fromString(value: String): Availability = valueOf(value.uppercase())
}
