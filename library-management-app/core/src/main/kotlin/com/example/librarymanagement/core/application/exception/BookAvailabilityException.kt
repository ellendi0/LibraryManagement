package com.example.librarymanagement.core.application.exception

class BookAvailabilityException(libraryId: String, bookId: String, availability: String) : RuntimeException() {
    override val message: String =
        "Book with id $bookId is ${availability.lowercase()} in library with id $libraryId"
}
