package com.example.com.example.gateway.exception

class EntityNotFoundException(entityName: String) : RuntimeException() {
    override val message: String = entityName
}
