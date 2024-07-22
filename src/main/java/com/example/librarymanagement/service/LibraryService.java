package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.BookPresence;
import com.example.librarymanagement.model.entity.Library;
import com.example.librarymanagement.model.enums.Availability;

import java.util.List;

public interface LibraryService {
    List<Library> findAll();
    Library getLibraryById(Long id);
    Library createLibrary(Library library);
    Library updateLibrary(Long id, Library updatedLibrary);
    Library addBookToLibrary(Long libraryId, Long bookId);
    List<BookPresence> getAllBooksByLibraryIdAndAvailability(Long libraryId, Availability availability);
    List<BookPresence> getAllBooksByLibraryId(Long libraryId);
    void removeBookFromLibrary(Long libraryId, Long bookId);
    void deleteLibrary(Long id);
}