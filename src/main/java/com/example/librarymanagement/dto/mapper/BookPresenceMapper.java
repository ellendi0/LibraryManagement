package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.model.entity.BookPresence;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookPresenceMapper {
    private final UserMapper userMapper;

    public BookPresenceMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public BookPresenceDto toBookPresenceDto(BookPresence bookPresence) {
        BookPresenceDto bookPresenceDto = new BookPresenceDto();
        bookPresenceDto.setId(bookPresence.getId());
        bookPresenceDto.setBookTitle(bookPresence.getBook().getTitle());
        bookPresenceDto.setBookAuthorId(bookPresence.getBook().getAuthor().getId());
        bookPresenceDto.setLibraryNameId(bookPresence.getLibrary().getId());
        bookPresenceDto.setUser(userMapper.toUserResponseDto(bookPresence.getUser()));
        bookPresenceDto.setAddress(bookPresence.getLibrary().getAddress());
        bookPresenceDto.setAvailability(bookPresence.getAvailability());
        return bookPresenceDto;
    }

    public List<BookPresenceDto> toBookPresenceDto(List<BookPresence> bookPresenceList) {
        if(bookPresenceList == null || bookPresenceList.isEmpty()) return new ArrayList<>();

        return bookPresenceList.stream()
                .map(this::toBookPresenceDto)
                .toList();
    }
}