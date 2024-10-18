package com.example.librarymanagement.reservation.domain

data class Reservation (
    val id: String? = null,
    val userId: String,
    val bookId: String,
    val libraryId: String,
)
