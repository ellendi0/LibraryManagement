package com.example.librarymanagement.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PublisherDto (
    val id: Long,

    @field:Size(min = 1, max = 50, message = "Publisher must contain no more than 50 characters")
    @field:NotBlank(message = "Publisher can't be blank")
    val name: String
)
