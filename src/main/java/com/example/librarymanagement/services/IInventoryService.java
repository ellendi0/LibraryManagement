package com.example.librarymanagement.services;

import com.example.librarymanagement.models.entities.Inventory;


import java.util.List;

public interface IInventoryService {
    Inventory getById(Long id);
    Inventory createInventory(Inventory inventory);
    List<Inventory> getByBookId(Long bookId);
    List<Inventory> getByLibraryId(Long libraryId);
    Inventory getByLibraryIdAndBookId(Long libraryId, Long bookId);
    Inventory updateInventory(Long id, Long bookId, Inventory updatedInventory);
    void deleteById(Long id);
}
