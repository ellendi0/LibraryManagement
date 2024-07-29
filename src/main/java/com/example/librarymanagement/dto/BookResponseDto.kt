package com.example.librarymanagement.dto

import com.example.librarymanagement.model.enums.Genre

class BookResponseDto (
    val id: Long,
    val title: String,
    val author: String,
    val publisher: String,
    val publishedYear: Int,
    val isbn: Long,
    val genre: Genre,
)