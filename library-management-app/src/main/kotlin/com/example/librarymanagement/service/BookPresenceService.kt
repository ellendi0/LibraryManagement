package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BookPresenceService {
    fun borrowBookFromLibrary(userId: String, libraryId: String, bookId: String): Flux<Journal>
    fun addBookToLibrary(libraryId: String, bookId: String): Mono<BookPresence>
    fun returnBookToLibrary(userId: String, libraryId: String, bookId: String): Flux<Journal>
    fun getAllByBookId(bookId: String): Flux<BookPresence>
    fun getAllByLibraryId(libraryId: String): Flux<BookPresence>
    fun getAllBookPresencesByLibraryIdAndBookId(libraryId: String, bookId: String): Flux<BookPresence>
    fun deleteBookPresenceById(id: String): Mono<Unit>
    fun existsAvailableBookInLibrary(bookId: String, libraryId: String): Mono<Boolean>
    fun existsBookPresenceByBookIdAndLibraryId(bookId: String, libraryId: String): Mono<Boolean>
}
