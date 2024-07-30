package com.example.librarymanagement.dto

data class UserResponseDto (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
)