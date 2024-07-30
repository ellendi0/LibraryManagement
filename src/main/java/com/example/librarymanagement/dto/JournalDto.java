package com.example.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalDto {
    private Long id;
    private LocalDate dateOfBorrowing;
    private LocalDate dateOfReturning;
    private String title;
    private String authorName;
    private String user;
    private String nameOfLibrary;
}