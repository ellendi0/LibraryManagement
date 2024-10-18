package com.example.librarymanagement.user.domain

data class User(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
)
