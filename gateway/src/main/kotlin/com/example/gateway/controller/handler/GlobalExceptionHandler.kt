package com.example.com.example.gateway.controller.handler

import com.example.com.example.gateway.exception.EntityNotFoundException
import com.example.gateway.model.ErrorDto
import com.example.gateway.model.mapper.ErrorMapper
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestControllerAdvice
class GlobalExceptionHandler(private val errorMapper: ErrorMapper) {
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(exception: EntityNotFoundException): Mono<ErrorDto> {
        return errorMapper.toErrorDto(HttpStatus.NOT_FOUND, listOf(exception.message)).toMono()
    }

    @ExceptionHandler(WebExchangeBindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: WebExchangeBindException): Mono<ErrorDto> {
        val errorMessages = ex.bindingResult.allErrors.map { it.defaultMessage ?: "Validation error" }.toList()
        return errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, errorMessages).toMono()
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): Mono<ErrorDto> {
        return exception.message?.let { errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, it) }.toMono()
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ErrorDto? {
        return exception.message?.let { errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, it) }
    }
}
