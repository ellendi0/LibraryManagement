package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Book

interface BookRepository{
    fun save(book: Book): Book
    fun findById(bookId: String): Book?
    fun findAll(): List<Book>
    fun deleteById(bookId: String)
    fun existsByIsbn(isbn: Long): Boolean
    fun findBookByTitleAndAuthorId(title: String, authorId: String): Book?
}
