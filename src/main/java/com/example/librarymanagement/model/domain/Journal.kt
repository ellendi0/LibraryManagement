package com.example.librarymanagement.model.domain

import java.time.LocalDate

data class Journal(
    val id: String? = null,
    var dateOfBorrowing: LocalDate,
    var dateOfReturning: LocalDate? = null,
    var userId: String,
    val bookPresenceId: String,
)
