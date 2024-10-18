package com.example.librarymanagement.publisher.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Publisher (
    @JsonProperty("id")
    val id: String? = null,
    @JsonProperty("name")
    val name: String,
)
