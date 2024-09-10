package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Author

interface AuthorRepository{
    fun save(author: Author): Author
    fun findById(authorId: String): Author?
    fun findAll(): List<Author>
}
