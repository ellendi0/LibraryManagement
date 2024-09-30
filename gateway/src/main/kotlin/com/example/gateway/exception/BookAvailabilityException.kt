package com.example.com.example.gateway.exception

class BookAvailabilityException(entityName: String) : RuntimeException() {
    override val message: String = entityName
}
