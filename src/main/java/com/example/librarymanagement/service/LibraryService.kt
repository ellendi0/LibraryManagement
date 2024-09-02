package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Library

interface LibraryService {
    fun findAll(): List<Library>
    fun getLibraryById(id: String): Library
    fun createLibrary(library: Library): Library
    fun updateLibrary(updatedLibrary: Library): Library
    fun deleteLibraryById(id: String)
}
