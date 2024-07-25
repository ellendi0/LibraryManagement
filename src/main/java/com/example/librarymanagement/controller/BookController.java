package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.BookResponseDto;
import com.example.librarymanagement.dto.mapper.BookMapper;
import com.example.librarymanagement.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponseDto> getAllBooks() {
        return bookMapper.toBookDto(bookService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookMapper.toBookDto(bookService.getBookById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto getBookByTitleAndAuthor(@RequestParam String title, @RequestParam Long author) {
        return bookMapper.toBookDto(bookService.getBookByTitleAndAuthor(title, author));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto createBook(@RequestBody @Valid BookRequestDto bookDto) {
        return bookMapper.toBookDto(bookService.createBook(bookDto.getAuthorId(), bookDto.getPublisherId(), bookMapper.toBook(bookDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto updateBook(@PathVariable Long id, @RequestBody @Valid BookRequestDto book) {
        return bookMapper.toBookDto(bookService.updateBook(id, bookMapper.toBook(book)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}