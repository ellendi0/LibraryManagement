package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.Library
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LibraryRepository: JpaRepository<Library, Long>
