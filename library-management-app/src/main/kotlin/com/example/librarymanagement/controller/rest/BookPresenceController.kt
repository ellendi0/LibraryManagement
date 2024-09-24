package com.example.librarymanagement.controller.rest

import com.example.librarymanagement.annotation.NotificationOnAvailability
import com.example.librarymanagement.dto.BookPresenceDto
import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.dto.mapper.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.service.BookPresenceService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1")
class BookPresenceController(
    private val bookPresenceService: BookPresenceService,
    private val bookPresenceMapper: BookPresenceMapper,
    private val journalMapper: JournalMapper
) {
    @PostMapping("/library/{libraryId}/book/{bookId}/presence")
    @ResponseStatus(HttpStatus.CREATED)
    fun addBookToLibrary(@PathVariable libraryId: String, @PathVariable bookId: String): Mono<BookPresenceDto> {
        return bookPresenceService.addBookToLibrary(libraryId, bookId).map { bookPresenceMapper.toBookPresenceDto(it) }
    }

    @GetMapping("/library/{libraryId}/book")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooksByLibraryId(@PathVariable libraryId: String): Flux<BookPresenceDto> {
        return bookPresenceService.getAllByLibraryId(libraryId).map { bookPresenceMapper.toBookPresenceDto(it) }
    }

    @GetMapping("/library/{libraryId}/book/{bookId}/presence")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooksByLibraryIdAndBookId(
        @PathVariable libraryId: String,
        @PathVariable bookId: String
    ): Flux<BookPresenceDto> {
        return bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(libraryId, bookId)
            .map { bookPresenceMapper.toBookPresenceDto(it) }
    }

    @DeleteMapping("/library/presence/{presenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeBookFromLibrary(@PathVariable presenceId: String): Mono<Unit> {
        return bookPresenceService.deleteBookPresenceById(presenceId)
    }

    @PostMapping("/user/{id}/borrowings")
    @ResponseStatus(HttpStatus.CREATED)
    fun borrowBookFromLibrary(
        @PathVariable(name = "id") userId: String,
        @RequestParam libraryId: String,
        @RequestParam bookId: String
    ): Flux<JournalDto> {
        return bookPresenceService.addUserToBook(userId, libraryId, bookId).map { journalMapper.toJournalDto(it) }
    }

    @PutMapping("/user/{id}/borrowings")
    @ResponseStatus(HttpStatus.OK)
    @NotificationOnAvailability
    fun returnBookToLibrary(
        @PathVariable(name = "id") userId: String,
        @RequestParam libraryId: String,
        @RequestParam bookId: String
    ): Flux<JournalDto> {
        return bookPresenceService.removeUserFromBook(userId, libraryId, bookId).map { journalMapper.toJournalDto(it) }
    }
}
