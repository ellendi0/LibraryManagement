package com.example.librarymanagement.services.impl;

import com.example.librarymanagement.models.entities.Inventory;
import com.example.librarymanagement.repositories.InventoryRepository;
import com.example.librarymanagement.services.IInventoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService implements IInventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory getById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entity not found for this id" + id));
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> getByBookId(Long bookId) {
        return inventoryRepository.findAllByBookId(bookId).orElseThrow(
                () -> new EntityNotFoundException("Entity not found for this id" + bookId));
    }

    @Override
    public List<Inventory> getByLibraryId(Long libraryId) {
        return inventoryRepository.findAllByLibraryId(libraryId).orElseThrow(
                () -> new EntityNotFoundException("Entity not found for this id" + libraryId));
    }

    @Override
    public Inventory getByLibraryIdAndBookId(Long libraryId, Long bookId) {
        return inventoryRepository.findByBookIdAndLibraryId(libraryId, bookId).orElseThrow(
                () -> new EntityNotFoundException("Entity not found for these id"));
    }

    @Override
    public Inventory updateInventory(Long id, Long bookId, Inventory updatedInventory) {
        Inventory inventory = getByLibraryIdAndBookId(id, bookId);

        inventory.setIsAvailable(updatedInventory.getIsAvailable());
        inventory.setIsReserved(updatedInventory.getIsReserved());
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        inventoryRepository.deleteById(id);
    }
}