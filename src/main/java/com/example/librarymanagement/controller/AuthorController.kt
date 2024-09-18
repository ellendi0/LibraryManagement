package com.example.librarymanagement.controller

import com.example.librarymanagement.dto.AuthorDto
import com.example.librarymanagement.dto.mapper.AuthorMapper
import com.example.librarymanagement.service.AuthorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/author")
class AuthorController(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllAuthors(): Flux<AuthorDto> = authorService.getAllAuthors().map { authorMapper.toAuthorDto(it) }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorById(@PathVariable id: String): Mono<AuthorDto> {
        return authorService.getAuthorById(id).map { authorMapper.toAuthorDto(it) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody @Valid authorDto: AuthorDto): Mono<AuthorDto> {
        return authorService.createAuthor(authorMapper.toAuthor(authorDto)).map { authorMapper.toAuthorDto(it) }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateAuthor(@PathVariable id: String, @RequestBody @Valid authorDto: AuthorDto): Mono<AuthorDto> {
        return authorService.updateAuthor(authorMapper.toAuthor(authorDto, id)).map { authorMapper.toAuthorDto(it) }
    }
}
