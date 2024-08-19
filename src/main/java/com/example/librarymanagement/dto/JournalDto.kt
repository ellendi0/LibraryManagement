package com.example.librarymanagement.dto

import java.time.LocalDate

data class JournalDto (
    val id: String? = null,
    val dateOfBorrowing: LocalDate,
    val dateOfReturning: LocalDate ?= null,
    val title: String,
    val author: String,
    val user: String,
    val nameOfLibrary: String,
)
