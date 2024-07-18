package com.example.librarymanagement.services.impl;

import com.example.librarymanagement.models.entities.Book;
import com.example.librarymanagement.repositories.BookRepository;
import com.example.librarymanagement.services.IBookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements IBookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with " + id + " not found"));
    }

    @Override
    public Book getBookByTitleAndAuthor(String title, String author) {
        return bookRepository.findBookByTitleAndAuthor(title, author).orElseThrow(
                () -> new EntityNotFoundException("Book with " + title + " not found")
        );
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book book = getBookById(id);

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setPublisher(updatedBook.getPublisher());
        book.setPublishedYear(updatedBook.getPublishedYear());
        book.setGenre(updatedBook.getGenre());

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        existsBook(id);
        bookRepository.deleteById(id);
    }

    private void existsBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with " + id + " not found");
        }
    }
}
