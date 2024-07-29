package com.example.librarymanagement.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class LibraryDto (
    val id: Long,

    @field:Size(min = 2, max = 50, message = "Name must contain at least 2 and no more than 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9',.-]+( [a-zA-Z0-9',.-]+)*$",
        message = "Library name must start with an uppercase letter")
    var name: String,

    @field:Size(min = 2, max = 100, message = "Address must contain at least 2 and no more than 100 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9\\s.,'-]+$", message = "Address must contain name and number of street")
    var address: String,
)