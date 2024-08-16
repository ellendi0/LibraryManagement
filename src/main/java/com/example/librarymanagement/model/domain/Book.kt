package com.example.librarymanagement.model.domain

import com.example.librarymanagement.model.enums.Genre

data class Book (
    val id: String? = null,
    val title: String,
    var author: Author?= null,
    var publisher: Publisher?= null,
    val publishedYear: Int,
    val isbn: Long,
    val genre: Genre,
    val bookPresence: MutableList<BookPresence> = mutableListOf(),
    val reservations: MutableList<Reservation> = mutableListOf()
)
