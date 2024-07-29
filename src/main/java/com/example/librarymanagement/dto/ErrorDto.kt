package com.example.librarymanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ErrorDto {
    private LocalDateTime timestamp;
    private int status;
    private List<String> message;
}