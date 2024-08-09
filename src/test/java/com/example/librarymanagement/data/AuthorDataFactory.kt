package com.example.librarymanagement.data

import com.example.librarymanagement.model.entity.Author

object AuthorDataFactory {
    fun createAuthor(): Author = Author(1L, "Author", "Author")
}
