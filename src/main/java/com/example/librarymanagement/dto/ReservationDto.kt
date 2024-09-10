package com.example.librarymanagement.dto

data class ReservationDto(
    val id: String? = null,
    val userId: String,
    val bookId: String,
    val libraryId: String? = null,
)
