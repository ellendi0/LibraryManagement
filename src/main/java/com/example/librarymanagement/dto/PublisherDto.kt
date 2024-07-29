package com.example.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDto {
    private Long id;

    @Size(max = 50, message = "Publisher must contain no more than 50 characters")
    @NotBlank(message = "Publisher can't be blank")
    private String name;
}