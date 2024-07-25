package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.AuthorDto;
import com.example.librarymanagement.model.entity.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper {
    public Author toAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        return author;
    }

    public AuthorDto toAuthorDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        return authorDto;
    }

    public List<AuthorDto> toAuthorDto(List<Author> authors) {
        if (authors == null || authors.isEmpty()) return new ArrayList<>();

        return authors.stream()
                .map(this::toAuthorDto)
                .toList();
    }
}