package com.example.librarymanagement.dto

import com.example.librarymanagement.model.enums.Availability

class BookPresenceDto (
    val id: String? = null,
    val user: UserResponseDto ?= null,
    val bookTitle: String,
    val bookAuthorId: String,
    val libraryId: String,
    val availability: Availability,
)
