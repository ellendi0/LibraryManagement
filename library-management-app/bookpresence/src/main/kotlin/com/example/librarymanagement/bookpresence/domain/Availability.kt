package com.example.librarymanagement.bookpresence.domain

import com.fasterxml.jackson.annotation.JsonCreator

enum class Availability {
    AVAILABLE, UNAVAILABLE;

    @JsonCreator
    fun fromString(value: String): Availability = valueOf(value.uppercase())
}
