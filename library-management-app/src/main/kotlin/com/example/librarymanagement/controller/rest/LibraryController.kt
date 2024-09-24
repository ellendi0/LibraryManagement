package com.example.librarymanagement.controller.rest

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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/library")
class LibraryController(
    private val libraryService: LibraryService,
    private val libraryMapper: LibraryMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllLibraries(): Flux<LibraryDto> = libraryService.findAll().map { libraryMapper.toLibraryDto(it) }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: String): Mono<LibraryDto> {
        return libraryService.getLibraryById(id).map { libraryMapper.toLibraryDto(it) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createLibrary(@RequestBody @Valid libraryDto: LibraryDto): Mono<LibraryDto> {
        return libraryService.createLibrary(libraryMapper.toLibrary(libraryDto)).map { libraryMapper.toLibraryDto(it) }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateLibrary(@PathVariable id: String, @RequestBody @Valid libraryDto: LibraryDto): Mono<LibraryDto> {
        return libraryService.updateLibrary(libraryMapper.toLibrary(libraryDto, id))
            .map { libraryMapper.toLibraryDto(it) }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteLibrary(@PathVariable id: String): Mono<Unit> = libraryService.deleteLibraryById(id)
}
