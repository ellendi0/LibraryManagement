package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository: JpaRepository<Author, Long>
