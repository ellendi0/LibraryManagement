package com.example.com.example.gateway.validation.model

import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BookDto(
    val id: String? = null,

    @field:Size(max = 100, message = "Title must contain no more than 100 characters")
    @field:NotBlank(message = "Title can't be blank")
    val title: String,

    @field:Digits(integer = 4, fraction = 0, message = "Year of publish must be in YYYY format")
    val publishedYear: Int,

    @field:Digits(integer = 13, message = "ISBN must contain 13 digits", fraction = 0)
    val isbn: Long,

    @field:NotNull(message = "Genre can't be empty")
    val genre: String,

    @field:NotBlank(message = "Author can't be empty")
    val authorId: String,

    @field:NotBlank(message = "Publisher can't be empty")
    val publisherId: String
)