package com.example.librarymanagement.exception

import com.example.librarymanagement.dto.ErrorDto
import com.example.librarymanagement.dto.mapper.ErrorMapper
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(private val errorMapper: ErrorMapper) {
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(exception: EntityNotFoundException): ErrorDto {
        return errorMapper.toErrorDto(HttpStatus.NOT_FOUND, exception.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ErrorDto {
        val errorMessages = exception.bindingResult.allErrors.map { it.defaultMessage }.toList()
        return errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, errorMessages as List<String>)
    }

    @ExceptionHandler(DuplicateKeyException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicateKeyException(exception: DuplicateKeyException): ErrorDto {
        return errorMapper.toErrorDto(HttpStatus.CONFLICT, exception.message)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ErrorDto {
        return errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ErrorDto {
        return errorMapper.toErrorDto(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(BookNotAvailableException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleBookNotAvailableException(exception: BookNotAvailableException): ErrorDto {
        return errorMapper.toErrorDto(HttpStatus.NOT_FOUND, exception.message)
    }

    @ExceptionHandler(ExistingReservationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleExistingReservationException(exception: ExistingReservationException): ErrorDto {
        return errorMapper.toErrorDto(HttpStatus.CONFLICT, exception.message)
    }
}