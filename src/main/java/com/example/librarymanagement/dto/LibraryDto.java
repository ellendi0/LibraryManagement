package com.example.librarymanagement.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LibraryDto {
    private Long id;

    @Size(min = 2, max = 50, message = "Name must contain at least 2 and no more than 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9',.-]+( [a-zA-Z0-9',.-]+)*$",
            message = "Library name must start with an uppercase letter")
    private String name;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "Address must contain at least 2 characters and no more than 100")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,'-]+$",
            message = "Address must contain name and number of street")
    private String address;
}