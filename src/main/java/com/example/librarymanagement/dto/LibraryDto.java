package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.Library;
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
    @Size(min = 2, max = 50, message = "Name must contain no more than 50 characters")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*( [A-Z][a-zA-Z]*)*$",
            message = "Library must start with an uppercase letter, contain alphabetic characters and spaces between words")
    private String name;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "Address must contain no more than 100 at least 2 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,'-]+$",
            message = "Address must contain name and number of street")
    private String address;

    public LibraryDto(Library library) {
        this.name = library.getName();
        this.address = library.getAddress();
    }
}