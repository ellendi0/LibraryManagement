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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/book")
class BookController(private val bookService: BookService, private val bookMapper: BookMapper) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooks(): Flux<BookDto> = bookService.findAll().map { bookMapper.toBookDto(it) }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getBookById(@PathVariable id: String): Mono<BookDto> =
        bookService.getBookById(id).map { bookMapper.toBookDto(it) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody @Valid bookDto: BookDto): Mono<BookDto> {
        return bookService.createBook(bookMapper.toBook(bookDto)).map { bookMapper.toBookDto(it) }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateBook(@PathVariable id: String, @RequestBody @Valid bookDto: BookDto): Mono<BookDto> {
        return bookService.updateBook(bookMapper.toBook(bookDto, id)).map { bookMapper.toBookDto(it) }
    }

    @GetMapping(params = ["title", "author"])
    @ResponseStatus(HttpStatus.OK)
    fun getBookByTitleAndAuthor(
        @RequestParam title: String,
        @RequestParam(name = "author") authorId: String
    ): Flux<BookDto> {
        return bookService.getBookByTitleAndAuthor(title, authorId).map { bookMapper.toBookDto(it) }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBookById(@PathVariable id: String): Mono<Unit> = bookService.deleteBookById(id)
}
