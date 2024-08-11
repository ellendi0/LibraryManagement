package com.example.librarymanagement.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFoundException(private val entityName: String) : RuntimeException() {
    override val message: String = "$entityName not found"
}
