package com.example.librarymanagement.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class ExistingReservationException(bookId: String, userId: String) : RuntimeException() {
    override val message: String = "User with id $userId already has a reservation for book with id $bookId"
}
