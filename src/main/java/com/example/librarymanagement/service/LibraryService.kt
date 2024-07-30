package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.enums.Availability

interface LibraryService {
    fun findAll(): List<Library>
    fun getLibraryById(id: Long): Library
    fun createLibrary(library: Library): Library
    fun updateLibrary(id: Long, updatedLibrary: Library): Library
    fun getAllBooksByLibraryIdAndAvailability(libraryId: Long, availability: Availability): List<BookPresence>
    fun getAllBooksByLibraryId(libraryId: Long): List<BookPresence>
    fun deleteLibrary(id: Long)
}