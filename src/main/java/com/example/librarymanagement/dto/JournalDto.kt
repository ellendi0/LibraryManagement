package com.example.librarymanagement.dto

import java.time.LocalDate

class JournalDto (
    val id: Long,
    val dateOfBorrowing: LocalDate,
    val dateOfReturning: LocalDate ?= null,
    val title: String,
    val author: String,
    val user: String,
    val nameOfLibrary: String,
)