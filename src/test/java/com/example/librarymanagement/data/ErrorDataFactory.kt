package com.example.librarymanagement.data

import com.example.librarymanagement.dto.ErrorDto
import org.springframework.http.HttpStatus

object ErrorDataFactory {
    fun createBadRequestError(): ErrorDto = ErrorDto(HttpStatus.BAD_REQUEST.value(), listOf("Invalid"))
    fun createNotFoundError(): ErrorDto = ErrorDto(HttpStatus.NOT_FOUND.value(), listOf("Entity"))
    fun createConflictError(): ErrorDto = ErrorDto(HttpStatus.CONFLICT.value(), listOf("Conflict"))
}

