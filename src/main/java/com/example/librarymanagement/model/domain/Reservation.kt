package com.example.librarymanagement.model.domain

data class Reservation (
    val id: String? = null,
    val userId: String,
    val bookId: String,
    val libraryId: String? = null,
)
