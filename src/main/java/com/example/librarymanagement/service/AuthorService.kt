package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.Author

interface AuthorService {
    fun createAuthor(author: Author): Author
    fun updateAuthor(id: Long, updatedAuthor: Author): Author
    fun getAuthorById(id: Long): Author
    fun getAllAuthors(): List<Author>
}