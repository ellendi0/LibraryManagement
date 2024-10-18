package com.example.librarymanagement.book.domain

data class Book(
    val id: String? = null,
    val title: String,
    var authorId: String,
    var publisherId: String,
    val publishedYear: Int,
    val isbn: Long,
    val genre: Genre,
)
