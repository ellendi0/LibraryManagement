package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Author

interface AuthorService {
    fun createAuthor(author: Author): Author
    fun updateAuthor(updatedAuthor: Author): Author
    fun getAuthorById(id: String): Author
    fun getAllAuthors(): List<Author>
}
