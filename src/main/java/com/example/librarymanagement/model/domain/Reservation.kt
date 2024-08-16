package com.example.librarymanagement.model.domain

data class Reservation (
    val id: String ?= null,
    val user: User,
    val book: Book,
    val library: Library,
)
