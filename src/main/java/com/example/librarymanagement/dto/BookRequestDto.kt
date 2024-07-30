package com.example.librarymanagement.dto

import com.example.librarymanagement.model.enums.Genre
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

class BookRequestDto(
    @field:Size(max = 100, message = "Title must contain no more than 100 characters")
    @field:NotBlank(message = "Title can't be blank")
    var title: String,

    @field:Digits(integer = 4, fraction = 0, message = "Year of publish must be in YYYY format")
    val publishedYear: Int,

    @field:Digits(integer = 13, message = "ISBN must contain 13 digits", fraction = 0)
    @field:NotNull(message = "ISBN can't be empty")
    val isbn: Long,

    @field:NotNull(message = "Genre can't be empty")
    val genre: Genre,

    @field:NotNull(message = "Author can't be empty")
    val authorId: Long,

    @field:NotNull(message = "Publisher can't be empty")
    val publisherId: Long
)
