package com.example.gateway.model

data class ErrorDto (
    val status: Int,
    val messages: List<String>
)
