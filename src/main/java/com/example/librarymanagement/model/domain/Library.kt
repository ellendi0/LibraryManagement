package com.example.librarymanagement.model.domain

data class Library (
    val id: String ?= null,
    val name: String,
    val address: String,
)
