package com.example.librarymanagement.repository

import com.example.librarymanagement.model.domain.Publisher

interface PublisherRepository{
    fun save(publisher: Publisher): Publisher
    fun findById(publisherId: String): Publisher?
    fun findAll(): List<Publisher>
}
