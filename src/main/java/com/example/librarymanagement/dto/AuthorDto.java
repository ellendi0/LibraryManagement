package com.example.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AuthorDto {
    private Long id;

    @Size(max = 50, message = "First name must contain no more than 50 characters")
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "First name must start with a capital letter followed by one or more lowercase letters")
    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @Size(max = 50, message = "Last name must contain no more than 50 characters")
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Last name must start with a capital letter followed by one or more lowercase letters")
    @NotBlank(message = "Last name can't be empty")
    private String lastName;
}