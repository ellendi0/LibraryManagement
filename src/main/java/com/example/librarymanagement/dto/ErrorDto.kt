package com.example.librarymanagement.dto

data class ErrorDto (
    val status: Int,
    val messages: List<String>
)
