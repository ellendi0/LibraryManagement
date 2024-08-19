package com.example.librarymanagement.controller

import com.example.librarymanagement.dto.LibraryDto
import com.example.librarymanagement.dto.mapper.LibraryMapper
import com.example.librarymanagement.service.LibraryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/library")
class LibraryController(
    private val libraryService: LibraryService,
    private val libraryMapper: LibraryMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun allLibraries(): List<LibraryDto> = libraryMapper.toLibraryDto(libraryService.findAll())

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: String): LibraryDto {
        return libraryMapper.toLibraryDto(libraryService.getLibraryById(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createLibrary(@RequestBody @Valid libraryDto: LibraryDto): LibraryDto {
        return libraryMapper.toLibraryDto(libraryService.createLibrary(libraryMapper.toLibrary(libraryDto)))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateLibrary(@PathVariable id: String, @RequestBody @Valid libraryDto: LibraryDto): LibraryDto {
        return libraryMapper.toLibraryDto(libraryService.updateLibrary(libraryMapper.toLibrary(libraryDto, id)))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteLibrary(@PathVariable id: String) {
        libraryService.deleteLibrary(id)
    }
}
