package com.example.librarymanagement.controller

import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.dto.BookResponseDto
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
    fun getAllBooks(): List<BookResponseDto> = bookMapper.toBookDto(bookService.findAll())

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getBookById(@PathVariable id: Long): BookResponseDto = bookMapper.toBookDto(bookService.getBookById(id))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody @Valid bookDto: BookRequestDto): BookResponseDto {
        return bookMapper
            .toBookDto(bookService.createBook(bookDto.authorId, bookDto.publisherId, bookMapper.toBook(bookDto)))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateBook(@PathVariable id: Long, @RequestBody @Valid bookDto: BookRequestDto): BookResponseDto {
        return bookMapper.toBookDto(bookService.updateBook(bookMapper.toBook(bookDto, id)))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Long) { bookService.deleteBook(id) }

    @GetMapping(params = ["title", "author"])
    @ResponseStatus(HttpStatus.OK)
    fun getBookByTitleAndAuthor(@RequestParam title: String, @RequestParam author: Long): BookResponseDto {
        return bookMapper.toBookDto(bookService.getBookByTitleAndAuthor(title, author))
    }
}
