package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Boolean existsByIsbn(Long isbn);
    Optional<Book> findBookByTitleAndAuthorId(String title, Long authorId);
}