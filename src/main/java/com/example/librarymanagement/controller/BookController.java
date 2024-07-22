package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookRequestDto;
import com.example.librarymanagement.dto.BookResponseDto;
import com.example.librarymanagement.dto.mapper.BookMapper;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return new ResponseEntity<>(BookMapper.toBookDto(bookService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        return new ResponseEntity<>(new BookResponseDto(bookService.getBookById(id)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<BookResponseDto> getBookByTitleAndAuthor(@RequestParam String title,
                                                                   @RequestParam Long author) {
        return new ResponseEntity<>(new BookResponseDto(bookService.getBookByTitleAndAuthor(title, author)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@RequestBody @Valid BookRequestDto bookDto) {
        return new ResponseEntity<>(new BookResponseDto(
                        bookService.createBook(bookDto.getAuthorId(), bookDto.getPublisherId(), BookMapper.toBook(bookDto))),
                        HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody @Valid BookRequestDto book) {
        return new ResponseEntity<>(new BookResponseDto(bookService.updateBook(id, BookMapper.toBook(book))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}