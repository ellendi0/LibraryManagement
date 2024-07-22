package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.BookPresenceDto;
import com.example.librarymanagement.model.entity.BookPresence;

import java.util.ArrayList;
import java.util.List;

public class BookPresenceMapper {
    public static List<BookPresenceDto> toBookPresenceDto(List<BookPresence> bookPresenceList) {
        List<BookPresenceDto> bookPresenceDtoList = new ArrayList<>();

        if(!bookPresenceList.isEmpty()){
            bookPresenceDtoList = bookPresenceList.stream().map(BookPresenceDto::new).toList();
        }
        return bookPresenceDtoList;
    }
}