package com.example.librarymanagement.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRequestDto(
    @field:Size(max = 50, message = "First name must contain no more than 50 characters")
    @field:Pattern(
        regexp = "[A-Z][a-z]+",
        message = "First name must start with a capital letter followed by one or more lowercase letters"
    )
    @field:NotBlank(message = "First name can't be empty")
    val firstName: String,

    @field:Size(max = 50, message = "Last name must contain no more than 50 characters")
    @field:Pattern(
        regexp = "[A-Z][a-z]+",
        message = "Last name must start with a capital letter followed by one or more lowercase letters"
    )
    @field:NotBlank(message = "Last name can't be empty")
    val lastName: String,

    @field:Email(message = "Invalid e-mail address")
    @field:NotBlank(message = "Email can't be empty")
    val email: String,

    @field:Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    val phoneNumber: String,

    @field:Pattern(
        regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}",
        message = "Password must be at least 6 characters, one uppercase letter and one number"
    )
    @field:NotBlank(message = "Password can't be empty")
    val password: String
)
