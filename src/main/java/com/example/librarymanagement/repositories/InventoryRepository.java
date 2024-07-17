package com.example.librarymanagement.repositories;

import com.example.librarymanagement.models.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
