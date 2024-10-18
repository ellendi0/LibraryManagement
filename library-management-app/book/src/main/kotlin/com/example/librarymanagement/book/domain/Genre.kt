package com.example.librarymanagement.book.domain

import com.fasterxml.jackson.annotation.JsonCreator

enum class Genre {
    POETRY, PROSE, DRAMA;

    @JsonCreator
    fun fromString(value: String): Genre = valueOf(value.uppercase())
}
