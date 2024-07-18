package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<List<Inventory>> findAllByBookId(Long bookId);
    Optional<List<Inventory>> findAllByLibraryId(Long libraryId);
    Optional<Inventory> findByBookIdAndLibraryId(Long bookId, Long libraryId);
}
