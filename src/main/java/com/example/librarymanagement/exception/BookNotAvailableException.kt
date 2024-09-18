package com.example.librarymanagement.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotAvailableException(libraryId: String, bookId: String) : RuntimeException() {
    override val message: String = "Book with id $bookId is not available in library with id $libraryId"
}
