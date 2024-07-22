package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.AuthorDto;
import com.example.librarymanagement.model.entity.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorMapper {
    public static Author toAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setPseudonym(authorDto.getPseudonym());
        return author;
    }

    public static List<AuthorDto> toAuthorDto(List<Author> authors) {
        List<AuthorDto> authorDtos = new ArrayList<>();

        if(!authors.isEmpty()){
            authorDtos = authors.stream().map(AuthorDto::new).toList();
        }
        return authorDtos;
    }
}