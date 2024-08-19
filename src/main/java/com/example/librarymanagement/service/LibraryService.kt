package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Library
import com.example.librarymanagement.model.enums.Availability

interface LibraryService {
    fun findAll(): List<Library>
    fun getLibraryById(id: String): Library
    fun createLibrary(library: Library): Library
    fun updateLibrary(updatedLibrary: Library): Library
    fun getAllBooksByLibraryIdAndAvailability(libraryId: String, availability: Availability): List<BookPresence>
    fun getAllBooksByLibraryId(libraryId: String): List<BookPresence>
    fun deleteLibrary(id: String)
}
