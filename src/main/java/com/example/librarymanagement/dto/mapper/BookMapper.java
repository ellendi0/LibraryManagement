package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.BookResponseDto;
import com.example.librarymanagement.model.entity.Book;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class BookMapper {
    public Book toBook(BookRequestDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setGenre(bookDto.getGenre());
        return book;
    }

    public BookResponseDto toBookDto(Book book) {
        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setId(book.getId());
        bookResponseDto.setTitle(book.getTitle());
        bookResponseDto.setAuthor(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        bookResponseDto.setPublisher(book.getPublisher().getName());
        bookResponseDto.setPublishedYear(book.getPublishedYear());
        bookResponseDto.setIsbn(book.getIsbn());
        bookResponseDto.setGenre(book.getGenre());
        return bookResponseDto;
    }

    public List<BookResponseDto> toBookDto(List<Book> books) {
        if(CollectionUtils.isEmpty(books)) return Collections.emptyList();

        return books.stream()
                .map(this::toBookDto)
                .toList();
    }
}