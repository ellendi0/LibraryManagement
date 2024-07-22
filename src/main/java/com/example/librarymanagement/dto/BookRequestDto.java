package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Publisher;
import com.example.librarymanagement.model.enums.Genre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
public class BookRequestDto {
    @Size(max = 100, message = "Title must contain no more than 100 characters")
    @NotBlank(message = "Title can't be blank")
    private String title;

    @Pattern(regexp = "^\\d{4}$\n", message = "Year of publish must be in YYYY format")
    private Integer publishedYear;

    @Length(message = "ISBN must contain 13 digits")
    @NotNull(message = "ISBN can't be empty")
    private Long isbn;

    @NotBlank(message = "Genre can't be empty")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    public BookRequestDto(Book book) {
        this.title = book.getTitle();
        this.publishedYear = book.getPublishedYear();
        this.isbn = book.getIsbn();
        this.genre = book.getGenre();
    }
}