package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.enums.Availability;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookPresenceDto {
    private Long id;
    private UserResponseDto user;
    private String bookTitle;
    private Long bookAuthorId;
    private Long libraryNameId;
    private String address;
    private Availability availability;
}