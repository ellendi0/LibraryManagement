package com.example.librarymanagement.service

import com.example.librarymanagement.model.domain.Publisher

interface PublisherService {
    fun createPublisher(publisher: Publisher): Publisher
    fun updatePublisher(updatedPublisher: Publisher): Publisher
    fun getPublisherById(id: String): Publisher
    fun getAllPublishers(): List<Publisher>
}
