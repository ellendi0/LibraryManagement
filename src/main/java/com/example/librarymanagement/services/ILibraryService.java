package com.example.librarymanagement.services;

import com.example.librarymanagement.models.entities.Inventory;
import com.example.librarymanagement.models.entities.Library;

import java.util.List;

public interface ILibraryService {
    List<Library> findAll();
    Library getById(Long id);
    Library createLibrary(Library library);
    Library updateLibrary(Long id, Library updatedLibrary);
    Library addBookToLibrary(Long libraryId, Long bookId);
    Library updateBookInLibrary(Long id, Long bookId, Inventory updatedInventory);
    void removeBookFromLibrary(Long libraryId, Long bookId);
    void deleteLibrary(Long id);
}
