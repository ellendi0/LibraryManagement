package com.example.librarymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotAvailableException extends RuntimeException {
    private Long libraryId;
    private Long bookId;

    public BookNotAvailableException(Long libraryId, Long bookId) {
        super(String.format("Book with id %d is not available in library with id %d", bookId, libraryId));
        this.libraryId = libraryId;
        this.bookId = bookId;
    }
}
