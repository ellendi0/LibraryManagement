package com.example.librarymanagement.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class PublisherDto (
    val id: Long,

    @field:Size(max = 50, message = "Publisher must contain no more than 50 characters")
    @field:NotBlank(message = "Publisher can't be blank")
    var name: String
)