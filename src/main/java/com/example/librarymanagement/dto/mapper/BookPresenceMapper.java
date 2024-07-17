package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.model.entity.BookPresence;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
        bookPresenceDto.setLibraryId(bookPresence.getLibrary().getId());
        bookPresenceDto.setUser(bookPresence.getUser() != null
                ? userMapper.toUserResponseDto(bookPresence.getUser()): null);
        bookPresenceDto.setAvailability(bookPresence.getAvailability());
        return bookPresenceDto;
    }

    public List<BookPresenceDto> toBookPresenceDto(List<BookPresence> bookPresenceList) {
        if(CollectionUtils.isEmpty(bookPresenceList)) return Collections.emptyList();

        return bookPresenceList.stream()
                .map(this::toBookPresenceDto)
                .toList();
    }
}