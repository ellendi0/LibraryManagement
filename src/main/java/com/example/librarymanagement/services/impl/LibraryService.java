package com.example.librarymanagement.services.impl;

import com.example.librarymanagement.models.entities.Book;
import com.example.librarymanagement.models.entities.Inventory;
import com.example.librarymanagement.models.entities.Library;
import com.example.librarymanagement.repositories.LibraryRepository;
import com.example.librarymanagement.services.IBookService;
import com.example.librarymanagement.services.IInventoryService;
import com.example.librarymanagement.services.ILibraryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService implements ILibraryService {

    private final LibraryRepository libraryRepository;
    private final IInventoryService inventoryService;
    private final IBookService iBookService;

    public LibraryService(LibraryRepository libraryRepository, IInventoryService inventoryService, IBookService bookService) {
        this.libraryRepository = libraryRepository;
        this.inventoryService = inventoryService;
        this.iBookService = bookService;
    }

    @Override
    public List<Library> findAll() {
        return libraryRepository.findAll();
    }

    @Override
    public Library getById(Long id) {
        return libraryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Library with id " + id + " not found"));
    }

    @Override
    public Library createLibrary(Library library) {
        return libraryRepository.save(library);
    }

    @Override
    @Transactional
    public Library addBookToLibrary(Long libraryId, Long bookId) {
        Library library = getById(libraryId);
        Book book = iBookService.getBookById(bookId);
        Inventory inventory = new Inventory(book, library);

        book.getInventory().add(inventory);
        library.getInventory().add(inventory);
        inventoryService.createInventory(inventory);

        return libraryRepository.save(library);
    }

    @Override
    @Transactional
    public Library updateBookInLibrary(Long id, Long bookId, Inventory updatedInventory) {
        return inventoryService.updateInventory(id, bookId, updatedInventory).getLibrary();
    }

    @Override
    @Transactional
    public void removeBookFromLibrary(Long libraryId, Long bookId) {
        inventoryService.deleteById(inventoryService.getByLibraryIdAndBookId(libraryId, bookId).getId());
    }

    @Override
    public Library updateLibrary(Long id, Library updatedLibrary) {
        Library library = getById(id);

        library.setName(updatedLibrary.getName());
        library.setAddress(updatedLibrary.getAddress());

        return libraryRepository.save(library);
    }

    @Override
    public void deleteLibrary(Long id) {
        if (!libraryRepository.existsById(id)) {
            throw new EntityNotFoundException("Library with id " + id + " not found");
        } else {
            libraryRepository.deleteById(id);
        }
    }
}
