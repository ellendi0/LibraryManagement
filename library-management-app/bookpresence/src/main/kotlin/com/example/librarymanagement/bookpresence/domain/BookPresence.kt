package com.example.librarymanagement.bookpresence.domain

data class BookPresence (
    val id: String? = null,
    var availability: Availability = Availability.AVAILABLE,
    val bookId: String,
    val libraryId: String,
    var userId: String? = null,
)
