package com.example.librarymanagement.service;

import com.example.librarymanagement.model.entity.Library;

import java.util.List;

public interface LibraryService {
    List<Library> findAll();
    Library getLibraryById(Long id);
    Library createLibrary(Library library);
    Library updateLibrary(Long id, Library updatedLibrary);
    void deleteLibrary(Long id);
}