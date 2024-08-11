package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long>{
    fun existsByIsbn(isbn: Long): Boolean
    fun findBookByTitleAndAuthorId(title: String, id: Long): Book?
}
