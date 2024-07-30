package com.example.librarymanagement.dto

import com.example.librarymanagement.model.enums.Availability

data class BookPresenceDto (
    val id: Long,
    val user: UserResponseDto ?= null,
    val bookTitle: String,
    val bookAuthorId: Long,
    val libraryId: Long,
    val availability: Availability,
)
