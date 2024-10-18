package com.example.librarymanagement.core.application.exception

class ExistingReservationException(bookId: String, userId: String) : RuntimeException() {
    override val message: String = "User with id $userId already has a reservation for book with id $bookId"
}
