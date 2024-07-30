package com.example.librarymanagement.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class DuplicateKeyException(resourceName: String, fieldName: String) : RuntimeException() {
    override val message: String = "$resourceName already exists with $fieldName"
}