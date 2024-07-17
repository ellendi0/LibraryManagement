package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
