package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.BookPresence;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookPresenceDto {
    private Long id;
    private UserResponseDto userResponseDto;
    private String bookTitle;
    private Long bookAuthorId;
    private Long libraryNameId;
    private String address;

    public BookPresenceDto(BookPresence bookPresence) {
        id = bookPresence.getId();
        userResponseDto = bookPresence.getUser() != null ? new UserResponseDto(bookPresence.getUser()) : null;
        bookTitle = bookPresence.getBook().getTitle();
        bookAuthorId = bookPresence.getBook().getAuthor().getId();
        libraryNameId = bookPresence.getLibrary().getId();
        address = bookPresence.getLibrary().getAddress();
    }
}