package com.example.com.example.gateway.exception.mapper

import com.example.internalapi.model.Error
import org.springframework.stereotype.Component

@Component
class ErrorMapper {
    fun toErrorProto(exception: Throwable): Error {
        return Error.newBuilder().setMessages(exception.message).build()
    }
}
