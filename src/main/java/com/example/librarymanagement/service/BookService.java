package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book getBookById(Long id);
    Book getBookByTitleAndAuthor(String title, Long authorId);
    Book createBook(Long authorId, Long publisherId, Book book);
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
}