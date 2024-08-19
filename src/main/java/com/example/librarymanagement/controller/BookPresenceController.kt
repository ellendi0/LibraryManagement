package com.example.librarymanagement.controller

import com.example.librarymanagement.dto.BookPresenceDto
import com.example.librarymanagement.dto.mapper.BookPresenceMapper
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.service.BookPresenceService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/library")
class BookPresenceController (
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper
) {
    @PostMapping("/{libraryId}/book/{bookId}/presence")
    @ResponseStatus(HttpStatus.CREATED)
    fun addBookToLibrary(@PathVariable libraryId: String, @PathVariable bookId: String): BookPresenceDto {
        return bookPresenceMapper.toBookPresenceDto(bookPresenceService.addBookToLibrary(libraryId, bookId))
    }

    @GetMapping("/{libraryId}/book")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooksByLibraryId(@PathVariable libraryId: String,
                               @RequestParam(required = false) availability: Availability?): List<BookPresenceDto> {
        return bookPresenceMapper.toBookPresenceDto(
            availability
                ?.let { bookPresenceService.getAllBookByLibraryIdAndAvailability(libraryId, it) }
                .run { bookPresenceService.getByLibraryId(libraryId) })
    }

    @GetMapping("/{libraryId}/book/{bookId}/presence")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooksByLibraryIdAndBookId(@PathVariable libraryId: String,
                                        @PathVariable bookId: String): List<BookPresenceDto> {
        return bookPresenceMapper
            .toBookPresenceDto(bookPresenceService.getAllBookByLibraryIdAndBookId(libraryId, bookId))
    }

    @DeleteMapping("/presence/{presenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeBookFromLibrary(@PathVariable presenceId: String) {
        bookPresenceService.deleteBookPresenceById(presenceId)
    }
}
