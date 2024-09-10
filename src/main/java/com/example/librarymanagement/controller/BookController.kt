package com.example.librarymanagement.controller

import com.example.librarymanagement.dto.BookDto
import com.example.librarymanagement.dto.mapper.BookMapper
import com.example.librarymanagement.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/book")
class BookController(private val bookService: BookService, private val bookMapper: BookMapper) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooks(): List<BookDto> = bookMapper.toBookDto(bookService.findAll())

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getBookById(@PathVariable id: String): BookDto = bookMapper.toBookDto(bookService.getBookById(id))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody @Valid bookDto: BookDto): BookDto {
        return bookMapper
            .toBookDto(bookService.createBook(bookMapper.toBook(bookDto)))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateBook(@PathVariable id: String, @RequestBody @Valid bookDto: BookDto): BookDto {
        return bookMapper
            .toBookDto(bookService.updateBook(bookMapper.toBook(bookDto, id)))
    }

    @GetMapping(params = ["title", "author"])
    @ResponseStatus(HttpStatus.OK)
    fun getBookByTitleAndAuthor(
        @RequestParam title: String,
        @RequestParam(name = "author") authorId: String
    ): BookDto {
        return bookMapper.toBookDto(bookService.getBookByTitleAndAuthor(title, authorId))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBookById(@PathVariable id: String) {
        bookService.deleteBookById(id)
    }
}
