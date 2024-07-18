package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
