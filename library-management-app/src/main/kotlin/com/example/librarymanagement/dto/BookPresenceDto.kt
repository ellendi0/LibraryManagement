package com.example.librarymanagement.dto

import com.example.librarymanagement.model.enums.Availability

class BookPresenceDto (
    val id: String? = null,
    val userId: String? = null,
    val bookId: String,
    val libraryId: String,
    val availability: Availability,
)
