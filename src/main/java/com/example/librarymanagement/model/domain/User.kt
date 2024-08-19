package com.example.librarymanagement.model.domain

data class User(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
)
