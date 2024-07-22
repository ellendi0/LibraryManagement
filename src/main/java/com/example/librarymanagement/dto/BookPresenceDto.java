package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.BookPresence;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookPresenceDto {
    private Long physicalIdentifier;
    private UserResponseDto userResponseDto;
    private String bookTitle;
    private String bookAuthor;
    private String libraryName;
    private String address;

    public BookPresenceDto(BookPresence bookPresence) {
        physicalIdentifier = bookPresence.getId();
        userResponseDto = bookPresence.getUser() != null ? new UserResponseDto(bookPresence.getUser()) : null;
        bookTitle = bookPresence.getBook().getTitle();
        bookAuthor = bookPresence.getBook().getAuthor().getFirstName() + " " + bookPresence.getBook().getAuthor().getLastName();
        libraryName = bookPresence.getLibrary().getName();
        address = bookPresence.getLibrary().getAddress();
    }
}