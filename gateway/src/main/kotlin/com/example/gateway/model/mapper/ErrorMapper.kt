package com.example.gateway.model.mapper

import com.example.gateway.model.ErrorDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ErrorMapper {
    fun toErrorDto(status: HttpStatus, messages: List<String>): ErrorDto {
        return ErrorDto(
            status = status.value(),
            messages = messages
        )
    }

    fun toErrorDto(status: HttpStatus, message: String): ErrorDto {
        return ErrorDto(
            status = status.value(),
            messages = listOf(message)
        )
    }
}
