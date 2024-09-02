package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Library

interface LibraryRepository{
    fun save(library: Library): Library
    fun findById(libraryId: String): Library?
    fun findAll(): List<Library>
    fun deleteById(libraryId: String)
}
