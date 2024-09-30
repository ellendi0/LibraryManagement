package com.example.com.example.gateway.validation.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

const val AUTHOR_NAMING_REGEX = "[A-Z][a-z]+"

data class AuthorDto(
    val id: String? = null,

    @field:Size(max = 50, message = "First name must contain no more than 50 characters")
    @field:Pattern(regexp = AUTHOR_NAMING_REGEX,
        message = "First name must start with a capital letter followed by one or more lowercase letters")
    @field:NotBlank(message = "First name can't be empty")
    val firstName: String,

    @field:Size(max = 50, message = "Last name must contain no more than 50 characters")
    @field:Pattern(regexp = AUTHOR_NAMING_REGEX,
        message = "Last name must start with a capital letter followed by one or more lowercase letters")
    @field:NotBlank(message = "Last name can't be empty")
    val lastName: String
)
