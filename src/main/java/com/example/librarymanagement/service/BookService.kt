package com.example.librarymanagement.service

import com.example.librarymanagement.model.jpa.JpaBook

interface BookService {
    fun findAll(): List<JpaBook>
    fun getBookById(id: Long): JpaBook
    fun getBookByTitleAndAuthor(title: String, authorId: Long): JpaBook
    fun createBook(authorId: Long, publisherId: Long, book: JpaBook): JpaBook
    fun updateBook(updatedBook: JpaBook): JpaBook
    fun deleteBook(id: Long)
}
