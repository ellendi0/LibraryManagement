package com.example.librarymanagement.core.infrastructure.convertor

import com.example.internalapi.model.Error
import org.springframework.stereotype.Component

@Component
class ErrorMapper {
    fun toErrorProto(exception: Throwable): Error {
        return Error.newBuilder().setMessages(exception.message).build()
    }
}
