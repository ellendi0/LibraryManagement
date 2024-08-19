package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Book

interface BookService {
    fun findAll(): List<Book>
    fun getBookById(id: String): Book
    fun getBookByTitleAndAuthor(title: String, authorId: String): Book
    fun createBook(authorId: String, publisherId: String, book: Book): Book
    fun updateBook(updatedBook: Book): Book
    fun deleteBook(id: String)
}
