package com.example.librarymanagement.repository

import com.example.librarymanagement.model.entity.Publisher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PublisherRepository: JpaRepository<Publisher, Long>
