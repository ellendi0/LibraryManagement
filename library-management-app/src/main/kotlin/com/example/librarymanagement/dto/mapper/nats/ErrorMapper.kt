package com.example.librarymanagement.dto.mapper.nats

import com.example.internalapi.model.Error
import com.example.librarymanagement.dto.ErrorDto
import com.example.librarymanagement.exception.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component("natsErrorMapper")
class ErrorMapper {
    fun toErrorProto(exception: Throwable): Error {
        return Error.newBuilder().setMessages(exception.message).build()
    }
}
