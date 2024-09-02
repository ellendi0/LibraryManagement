package com.example.librarymanagement.model.domain

import com.example.librarymanagement.model.enums.Availability

data class BookPresence (
    val id: String? = null,
    var availability: Availability = Availability.AVAILABLE,
    val bookId: String,
    val libraryId: String,
    var userId: String? = null,
)
