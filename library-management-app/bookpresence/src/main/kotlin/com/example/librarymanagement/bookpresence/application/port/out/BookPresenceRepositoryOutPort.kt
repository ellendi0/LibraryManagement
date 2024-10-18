package com.example.librarymanagement.bookpresence.application.port.out

import com.example.librarymanagement.bookpresence.domain.Availability
import com.example.librarymanagement.bookpresence.domain.BookPresence
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BookPresenceRepositoryOutPort {
    fun saveOrUpdate(bookPresence: BookPresence): Mono<BookPresence>
    fun addBookToUser(userId: String, libraryId: String, bookId: String): Mono<BookPresence>
    fun deleteById(bookPresenceId: String): Mono<Unit>
    fun findAllByBookId(bookId: String): Flux<BookPresence>
    fun findAllByLibraryId(libraryId: String): Flux<BookPresence>
    fun findAllByLibraryIdAndBookId(
        libraryId: String,
        bookId: String
    ): Flux<BookPresence>

    fun findAllByLibraryIdAndBookIdAndAvailability(
        libraryId: String,
        bookId: String,
        availability: Availability
    ): Flux<BookPresence>

    fun existsByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean>
}
