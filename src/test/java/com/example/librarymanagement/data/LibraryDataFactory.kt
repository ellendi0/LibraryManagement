package com.example.librarymanagement.data

import com.example.librarymanagement.model.entity.Library

object LibraryDataFactory {
    fun createLibrary(): Library = Library(1L, "Library", "Address")
}
