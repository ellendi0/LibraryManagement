package com.example.librarymanagement.core.application.exception

class EntityNotFoundException(entityName: String) : RuntimeException() {
    override val message: String = "$entityName not found"
}
