package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByTitleAndAuthor(String title, String author);
}
