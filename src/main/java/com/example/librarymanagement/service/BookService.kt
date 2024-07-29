package com.example.librarymanagement.service

import com.example.librarymanagement.model.entity.Book

interface BookService {
    fun findAll(): List<Book>
    fun getBookById(id: Long): Book
    fun getBookByTitleAndAuthor(title: String, authorId: Long): Book
    fun createBook(authorId: Long, publisherId: Long, book: Book): Book
    fun updateBook(id: Long, updatedBook: Book): Book
    fun deleteBook(id: Long)
}