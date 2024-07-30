package com.example.librarymanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private String bookTitle;
    private String author;
    private String nameOfLibrary;
}