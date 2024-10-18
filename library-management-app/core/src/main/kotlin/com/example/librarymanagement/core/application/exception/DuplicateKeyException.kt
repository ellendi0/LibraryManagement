package com.example.librarymanagement.core.application.exception

class DuplicateKeyException(resourceName: String, fieldName: String) : RuntimeException() {
    override val message: String = "$resourceName already exists with $fieldName"
}
