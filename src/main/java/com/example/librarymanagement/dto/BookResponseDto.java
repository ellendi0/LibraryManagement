package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.enums.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Integer publishedYear;
    private Long isbn;
    private Genre genre;
}