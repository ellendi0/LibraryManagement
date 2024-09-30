package com.example.com.example.gateway.controller.handler

import com.example.gateway.exception.EntityNotFoundException
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.support.WebExchangeBindException

@GrpcAdvice
class GlobalExceptionHandler {

    @GrpcExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(exception: EntityNotFoundException): StatusRuntimeException {
        val errorMessage = exception.message
        return Status.NOT_FOUND.withDescription(errorMessage).asRuntimeException()
    }

    @GrpcExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): StatusRuntimeException {
        val errorMessage = exception.message ?: "Illegal argument"
        return Status.INVALID_ARGUMENT.withDescription(errorMessage).asRuntimeException()
    }

    @GrpcExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): StatusRuntimeException {
        val errorMessage = exception.message ?: "Malformed request"
        return Status.INVALID_ARGUMENT.withDescription(errorMessage).asRuntimeException()
    }

    @GrpcExceptionHandler(WebExchangeBindException::class)
    fun handleValidationExceptions(ex: WebExchangeBindException): StatusRuntimeException {
        val errorMessages = ex.bindingResult.allErrors.joinToString(", ") {
            it.defaultMessage ?: "Validation error"
        }
        return Status.INVALID_ARGUMENT.withDescription(errorMessages).asRuntimeException()
    }

    @GrpcExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception): StatusRuntimeException {
        val errorMessage = exception.message ?: "Unknown error occurred"
        return Status.UNKNOWN.withDescription(errorMessage).asRuntimeException()
    }
}
