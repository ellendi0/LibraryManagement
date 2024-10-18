package com.example.librarymanagement.exception

import com.example.librarymanagement.model.enums.Availability
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookAvailabilityException(libraryId: String, bookId: String, availability: Availability) : RuntimeException() {
    override val message: String =
        "Book with id $bookId is ${availability.toString().lowercase()} in library with id $libraryId"
}
