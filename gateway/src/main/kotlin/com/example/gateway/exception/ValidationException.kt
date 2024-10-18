package com.example.com.example.gateway.exception

class ValidationException(entityName: String) : RuntimeException() {
    override val message: String = entityName
}
