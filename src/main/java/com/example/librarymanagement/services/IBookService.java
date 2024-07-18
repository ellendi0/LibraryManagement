package com.example.librarymanagement.services;

import com.example.librarymanagement.models.entities.Book;

import java.util.List;

public interface IBookService {
    List<Book> findAll();
    Book getBookById(Long id);
    Book getBookByTitleAndAuthor(String title, String author);
    Book createBook(Book book);
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
}
