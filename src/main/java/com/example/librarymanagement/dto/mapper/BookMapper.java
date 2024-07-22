package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.BookResponseDto;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.enums.Genre;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {
    public static Book toBook(BookRequestDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setGenre(Genre.fromString(bookDto.getGenre()));
        return book;
    }

    public static List<BookResponseDto> toBookDto(List<Book> books) {
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();

        if(!books.isEmpty()){
            bookResponseDtos = books.stream().map(BookResponseDto::new).toList();
        }
        return bookResponseDtos;
    }
}