package com.example.librarymanagement.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class AuthorDto(
    val id: Long? = null,

    @field:Size(max = 50, message = "First name must contain no more than 50 characters")
    @field:Pattern(regexp = "[A-Z][a-z]+", message = "First name must start with a capital letter followed by one or more lowercase letters")
    @field:NotBlank(message = "First name can't be empty")
    val firstName: String,

    @field:Size(max = 50, message = "Last name must contain no more than 50 characters")
    @field:Pattern(regexp = "[A-Z][a-z]+", message = "Last name must start with a capital letter followed by one or more lowercase letters")
    @field:NotBlank(message = "Last name can't be empty")
    var lastName: String
)
