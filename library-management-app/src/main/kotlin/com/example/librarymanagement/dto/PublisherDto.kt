package com.example.librarymanagement.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PublisherDto (
    val id: String? = null,

    @field:Size(min = 1, max = 50, message = "Publisher name must contain no more than 50 characters")
    @field:NotBlank(message = "Publisher name can't be blank")
    val name: String
)
