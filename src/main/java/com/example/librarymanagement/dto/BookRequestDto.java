package com.example.librarymanagement.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookRequestDto {
    @Size(max = 100, message = "Title must contain no more than 100 characters")
    @NotBlank(message = "Title can't be blank")
    private String title;

    @Digits(integer = 4, fraction = 0, message = "Year of publish must be in YYYY format")
    private Integer publishedYear;

    @Digits(integer = 13, message = "ISBN must contain 13 digits", fraction = 0)
    @NotNull(message = "ISBN can't be empty")
    private Long isbn;

    @NotNull(message = "Genre can't be empty")
    private String genre;

    @NotNull(message = "Author can't be empty")
    private Long authorId;

    @NotNull(message = "Publisher can't be empty")
    private Long publisherId;
}