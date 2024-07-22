package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.AuthorDto;
import com.example.librarymanagement.model.entity.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {
    public static List<AuthorDto> toAuthorDto(List<Author> authors) {
        List<AuthorDto> authorDtos = new ArrayList<>();

        if(!authors.isEmpty()){
            authorDtos = authors.stream().map(AuthorDto::new).toList();
        }
        return authorDtos;
    }
}