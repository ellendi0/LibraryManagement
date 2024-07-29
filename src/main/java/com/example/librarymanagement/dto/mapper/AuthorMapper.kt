package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.AuthorDto;
import com.example.librarymanagement.model.entity.Author;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        if(CollectionUtils.isEmpty(authors)) return Collections.emptyList();

        return authors.stream()
                .map(this::toAuthorDto)
                .toList();
    }
}