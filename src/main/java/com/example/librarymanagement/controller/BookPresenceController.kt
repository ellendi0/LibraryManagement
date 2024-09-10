package com.example.librarymanagement.controller

import com.example.librarymanagement.annotation.NotificationOnAvailability
import com.example.librarymanagement.dto.BookPresenceDto
import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.dto.mapper.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.JournalMapper
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
@RequestMapping("/api/v1")
class BookPresenceController(
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper,
    private val journalMapper: JournalMapper
) {
    @PostMapping("/library/{libraryId}/book/{bookId}/presence")
    @ResponseStatus(HttpStatus.CREATED)
    fun addBookToLibrary(@PathVariable libraryId: String, @PathVariable bookId: String): BookPresenceDto {
        return bookPresenceMapper.toBookPresenceDto(bookPresenceService.addBookToLibrary(libraryId, bookId))
    }

    @GetMapping("/library/{libraryId}/book")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooksByLibraryId(
        @PathVariable libraryId: String,
        @RequestParam(required = false) availability: Availability?
    ): List<BookPresenceDto> {
        return bookPresenceMapper.toBookPresenceDto(
            availability
                ?.let { bookPresenceService.getAllBookPresencesByLibraryIdAndAvailability(libraryId, it) }
                .run { bookPresenceService.getAllByLibraryId(libraryId) })
    }

    @GetMapping("/library/{libraryId}/book/{bookId}/presence")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooksByLibraryIdAndBookId(
        @PathVariable libraryId: String,
        @PathVariable bookId: String
    ): List<BookPresenceDto> {
        return bookPresenceMapper
            .toBookPresenceDto(bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(libraryId, bookId))
    }

    @DeleteMapping("/library/presence/{presenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeBookFromLibrary(@PathVariable presenceId: String) {
        bookPresenceService.deleteBookPresenceById(presenceId)
    }

    @PostMapping("/user/{id}/borrowings")
    @ResponseStatus(HttpStatus.CREATED)
    fun borrowBookFromLibrary(
        @PathVariable(name = "id") userId: String,
        @RequestParam libraryId: String,
        @RequestParam bookId: String
    ): List<JournalDto> {
        return journalMapper.toJournalDto(bookPresenceService.addUserToBook(userId, libraryId, bookId))
    }

    @DeleteMapping("/user/{id}/borrowings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @NotificationOnAvailability
    fun returnBookToLibrary(
        @PathVariable(name = "id") userId: String,
        @RequestParam libraryId: String,
        @RequestParam bookId: String
    ): List<JournalDto> {
        return journalMapper.toJournalDto(bookPresenceService.removeUserFromBook(userId, libraryId, bookId))
    }
}
