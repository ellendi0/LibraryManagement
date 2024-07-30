package com.example.librarymanagement.dto

data class ReservationDto(
    var id: Long,
    val bookTitle: String,
    val author: String,
    val nameOfLibrary: String? = null
)