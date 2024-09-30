package com.example.com.example.gateway.exception

class DuplicateKeyException(entityName: String) : RuntimeException() {
    override val message: String = entityName
}
