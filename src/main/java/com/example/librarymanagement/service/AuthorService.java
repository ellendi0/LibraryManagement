package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Author;

import java.util.List;

public interface AuthorService {
    Author createAuthor(Author author);
    Author updateAuthor(Long id, Author updatedAuthor);
    Author getAuthorById(Long id);
    List<Author> getAllAuthors();
}