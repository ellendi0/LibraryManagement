package com.example.librarymanagement.dto

import java.time.LocalDate

data class JournalDto (
    val id: String?,
    val dateOfBorrowing: LocalDate?,
    val dateOfReturning: LocalDate? = null,
    val bookPresenceId: String,
    val userId: String,
)
