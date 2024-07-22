package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.enums.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookResponseDto {
    private String title;
    private AuthorDto author;
    private PublisherDto publisher;
    private Integer publishedYear;
    private Long isbn;
    private Genre genre;

    public BookResponseDto(Book book) {
        this.title = book.getTitle();
        this.author = new AuthorDto(book.getAuthor());
        this.publisher = new PublisherDto(book.getPublisher());
        this.publishedYear = book.getPublishedYear();
        this.isbn = book.getIsbn();
        this.genre = book.getGenre();
    }
}