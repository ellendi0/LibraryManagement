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

@RestController
@RequestMapping("/api/v1/author")
class AuthorController(private val authorService: AuthorService, private val authorMapper: AuthorMapper){

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllAuthors(): List<AuthorDto> = authorMapper.toAuthorDto(authorService.getAllAuthors())

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAuthorById(@PathVariable id: Long): AuthorDto = authorMapper.toAuthorDto(authorService.getAuthorById(id))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody @Valid authorDto: AuthorDto): AuthorDto {
        return authorMapper.toAuthorDto(authorService.createAuthor(authorMapper.toAuthor(authorDto)))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateAuthor(@PathVariable id: Long, @RequestBody @Valid authorDto: AuthorDto): AuthorDto {
        return authorMapper.toAuthorDto(authorService.updateAuthor(id, authorMapper.toAuthor(authorDto)))
    }
}