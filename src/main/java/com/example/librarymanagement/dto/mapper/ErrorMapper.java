package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErrorMapper {
    public ErrorDto toErrorDto(HttpStatus status, List<String> message) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(status.value());
        errorDto.setMessage(message);
        return errorDto;
    }

    public ErrorDto toErrorDto(HttpStatus status, String message) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(status.value());
        errorDto.setMessage(List.of(message));
        return errorDto;
    }
}